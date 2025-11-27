package ru.yandex.practicum.telemetry.collector.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaEventService {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventService.class);

    private final Producer<String, SpecificRecordBase> producer;

    public KafkaEventService(Properties config) {
        // Используем StringSerializer для ключей
        config.putIfAbsent("key.serializer", StringSerializer.class.getName());
        config.putIfAbsent("value.serializer", GeneralAvroSerializer.class.getName());

        this.producer = new KafkaProducer<>(config);
        log.info("KafkaEventSender initialized");
    }

    /**
     * Асинхронная отправка события с ключом
     */
    public void send(String topic, String key, SpecificRecordBase event) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, key, event);

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Ошибка отправки в топик '{}': {}", topic, exception.getMessage());
            } else {
                log.debug("Сообщение отправлено в топик '{}' [partition: {}, offset: {}]",
                        topic, metadata.partition(), metadata.offset());
            }
        });
    }

    /**
     * Асинхронная отправка события без ключа (key = null)
     */
    public void send(String topic, SpecificRecordBase event) {
        send(topic, null, event);
    }

    /**
     * Закрытие producer с graceful shutdown
     */
    public void close() {
        if (producer != null) {
            try {
                producer.flush();
                producer.close();
                log.info("KafkaEventSender успешно закрыт");
            } catch (Exception e) {
                log.error("Ошибка при закрытии KafkaEventSender: {}", e.getMessage());
            }
        }
    }
}