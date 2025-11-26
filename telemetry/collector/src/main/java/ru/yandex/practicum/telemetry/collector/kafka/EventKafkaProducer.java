package ru.yandex.practicum.telemetry.collector.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEvent;

import java.util.Properties;

public class EventKafkaProducer {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        try(Producer<String, HubEvent> producer =  new KafkaProducer<>(config)) {
        }
    }
}