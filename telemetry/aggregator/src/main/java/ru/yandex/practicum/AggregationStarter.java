package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.SnapshotService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@Slf4j
@Component
public class AggregationStarter {
    private final SnapshotService snapshotService;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final String sensorsTopic;
    private final String snapshotsTopic;

    private volatile boolean running = true;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Map<TopicPartition, OffsetAndMetadata> pendingOffsets = new ConcurrentHashMap<>();
    private final List<Future<RecordMetadata>> pendingFutures = new CopyOnWriteArrayList<>();

    public AggregationStarter(SnapshotService snapshotService, KafkaConfig kafkaConfig) {
        this.snapshotService = snapshotService;

        // Получаем конфигурации
        KafkaConfig.ConsumerConfig consumerConfig = kafkaConfig.getConsumer();
        KafkaConfig.ProducerConfig producerConfig = kafkaConfig.getProducer();

        this.sensorsTopic = consumerConfig.getTopic();
        this.snapshotsTopic = producerConfig.getTopic();

        // Создаем consumer и producer
        this.consumer = new KafkaConsumer<>(consumerConfig.getProperties());
        this.producer = new KafkaProducer<>(producerConfig.getProperties());

        setupShutdownHook();
        log.info("AggregationStarter initialized. Sensors topic: {}, Snapshots topic: {}",
                sensorsTopic, snapshotsTopic);
    }

    private void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown hook triggered");
            running = false;
            consumer.wakeup();
            executorService.shutdown();
        }));
    }

    /**
     * Запуск агрегации данных
     */
    public void start() {
        log.info("Starting aggregator...");

        try {
            consumer.subscribe(List.of(sensorsTopic));
            log.info("Subscribed to topic: {}", sensorsTopic);

            while (running) {
                ConsumerRecords<String, SensorEventAvro> records =
                        consumer.poll(Duration.ofMillis(1000));

                if (!records.isEmpty()) {
                    log.debug("Received {} records", records.count());
                    processBatch(records);
                }
            }
        } catch (WakeupException e) {
            log.info("Consumer woken up for shutdown");
        } catch (Exception e) {
            log.error("Error in aggregator main loop", e);
        } finally {
            gracefulShutdown();
        }
    }

    /**
     * Обработка пачки сообщений
     */
    private void processBatch(ConsumerRecords<String, SensorEventAvro> records) {
        pendingFutures.clear();
        pendingOffsets.clear();

        for (ConsumerRecord<String, SensorEventAvro> record : records) {
            try {
                processRecord(record);
            } catch (Exception e) {
                log.error("Failed to process record at offset {}: {}",
                        record.offset(), e.getMessage());
                // Не сохраняем offset при ошибке обработки
            }
        }

        // Ждем завершения всех отправок
        waitForAllFutures();

        // Коммитим offsets только после успешной отправки всех снапшотов
        if (!pendingOffsets.isEmpty()) {
            commitOffsets();
        }
    }

    /**
     * Обработка одного сообщения
     */
    private void processRecord(ConsumerRecord<String, SensorEventAvro> record) {
        SensorEventAvro event = record.value();
        log.debug("Processing event - hub: {}, device: {}, offset: {}",
                event.getHubId(), event.getId(), record.offset());

        Optional<SensorsSnapshotAvro> updatedState = snapshotService.updateState(event);

        if (updatedState.isPresent()) {
            SensorsSnapshotAvro snapshot = updatedState.get();
            sendSnapshotAsync(snapshot, record);
        } else {
            log.trace("No update for hub: {}, device: {}",
                    event.getHubId(), event.getId());
            // Все равно сохраняем offset если обработка успешна
            saveOffsetForCommit(record);
        }
    }

    /**
     * Асинхронная отправка снапшота
     */
    private void sendSnapshotAsync(SensorsSnapshotAvro snapshot, ConsumerRecord<String, SensorEventAvro> sourceRecord) {
        ProducerRecord<String, SensorsSnapshotAvro> recordToSend = new ProducerRecord<>(
                snapshotsTopic,
                null,
                snapshot.getTimestamp().toEpochMilli(),
                snapshot.getHubId(),
                snapshot
        );

        Future<RecordMetadata> future = producer.send(recordToSend, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to send snapshot for hub: {}", snapshot.getHubId(), exception);
            } else {
                log.debug("Snapshot sent successfully - hub: {}, partition: {}, offset: {}",
                        snapshot.getHubId(), metadata.partition(), metadata.offset());
                // Сохраняем offset только после успешной отправки
                saveOffsetForCommit(sourceRecord);
            }
        });

        pendingFutures.add(future);
    }

    /**
     * Сохранение offset для последующего коммита
     */
    private void saveOffsetForCommit(ConsumerRecord<String, SensorEventAvro> record) {
        TopicPartition tp = new TopicPartition(record.topic(), record.partition());
        pendingOffsets.put(tp, new OffsetAndMetadata(record.offset() + 1));
    }

    /**
     * Ожидание завершения всех асинхронных отправок
     */
    private void waitForAllFutures() {
        for (Future<RecordMetadata> future : pendingFutures) {
            try {
                future.get(5, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                log.warn("Timeout waiting for producer response");
            } catch (Exception e) {
                log.warn("Error waiting for producer future", e);
            }
        }
    }

    /**
     * Коммит offsets
     */
    private void commitOffsets() {
        try {
            consumer.commitSync(pendingOffsets);
            log.debug("Committed offsets for {} partition(s)", pendingOffsets.size());
        } catch (Exception e) {
            log.error("Failed to commit offsets", e);
            // При ошибке коммита offsets будут обработаны повторно при следующем poll
        }
    }

    /**
     * Грациозное завершение работы
     */
    private void gracefulShutdown() {
        log.info("Starting graceful shutdown...");

        try {
            // 1. Ждем завершения всех отправок
            producer.flush();

            // 2. Коммитим оставшиеся offsets
            if (!pendingOffsets.isEmpty()) {
                consumer.commitSync(pendingOffsets);
            }

            // 3. Закрываем ресурсы
            consumer.close(Duration.ofSeconds(5));
            producer.close(Duration.ofSeconds(5));
            executorService.shutdown();

            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }

            log.info("Aggregator shutdown completed successfully");

        } catch (Exception e) {
            log.error("Error during shutdown", e);
        }
    }

    /**
     * Вспомогательный метод для получения статистики
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("running", running);
        stats.put("pendingFutures", pendingFutures.size());
        stats.put("pendingOffsets", pendingOffsets.size());
        return stats;
    }
}