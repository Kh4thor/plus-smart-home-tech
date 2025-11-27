package ru.yandex.practicum.telemetry.collector.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;


/**
 * Класс для отправки сообщений в кафку
 */
@Slf4j
@Component
public class KafkaEventProducer implements AutoCloseable {

    protected final KafkaProducer<String, SpecificRecordBase> producer;
    protected final EnumMap<KafkaConfig.TopicType, String> topics;

    /**
     * Конструктор класса.
     *
     * @param kafkaConfig Класс содержащий настройки для работы с kafka
     */
    public KafkaEventProducer(KafkaConfig kafkaConfig) {
        this.topics = kafkaConfig.getProducer().getTopics();

        // Создаём продюсера используя настройки из конфигурации приложения
        this.producer = new KafkaProducer<>(kafkaConfig.getProducer().getProperties());
    }

    /**
     * Обрабатывает событие от датчика и сохраняет его в топик Kafka.
     * @param event     Событие от датчика
     * @param hubId     Идентификатор хаба, в котором зарегистрирован датчик
     * @param timestamp Метка времени, когда произошло событие
     * @param topicType Тип топика который нужно использовать для отправки сообщения
     */
    public void send(SpecificRecordBase event, String hubId, Instant timestamp, KafkaConfig.TopicType topicType) {
        String topic = topics.get(topicType);
        // Формируем запись для отправки в топик, при этом указываем ключ записи - это id хаба
        // это означает, что запись будет сохраняться в партицию в зависимости от id хаба, а это
        // в свою очередь означает, что записи относящиеся к одному хабу можно будет читать упорядоченно
        // т.к. кафка гарантирует очередность сообщений только в рамках партиции.
        // Также мы указываем таймстемп записи и используем для этого время возникновения события
        // это значит, что кафка будет упорядочивать записи по времени возникновения события, а не времени
        // когда брокер кафки получил сообщение
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,                    // Имя топика куда будет осуществлена запись
                null,                     // Номер партиции (если null, то используется ключ для вычисления раздела)
                timestamp.toEpochMilli(), // Метка времени события
                hubId,                    // Ключ события
                event                     // Значение события
        );

        // Логирование сохранения события
        log.trace("Сохраняю событие {} связанное с хабом {} в топик {}",
                event.getClass().getSimpleName(), hubId, topic);

        // Отправка события в топик Kafka
        producer.send(record);
    }

    /**
     * Метод для закрытия ресурсов, связанных с обработчиком.
     * Завершает отправку сообщений в Kafka и закрывает продюсера.
     */
    @Override
    public void close() {
        // Данный метод из AutoCloseable, Spring будет автоматически добавлять бины
        // которые реализуют AutoCloseable/Disposable в shutdown hook при закрытии jvm
        // и вызывать их метод close

        // отправляем оставшиеся данные и закрываем продюсер
        producer.flush();
        producer.close(Duration.ofSeconds(10));
    }
}