package ru.yandex.practicum.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.hubevent.HubEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.service.HubEventHandler;
import ru.yandex.practicum.service.SensorEventHandler;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaEventSenderImpl implements KafkaEventSender, DisposableBean {
    private final HubEventHandler hubEventHandler;
    private final SensorEventHandler sensorEventHandler;
    private final Producer<String, SensorEventAvro> sensorProducer;
    private final Producer<String, HubEventAvro> hubProducer;

    @Value("${kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${kafka.topic.sensors:telemetry.sensors.v1}")
    private String sensorsTopic;

    @Value("${kafka.topic.hubs:telemetry.hubs.v1}")
    private String hubsTopic;

    @Value("${kafka.producer.acks:all}")
    private String acks;

    @Value("${kafka.producer.retries:3}")
    private String retries;

    @Value("${kafka.producer.request-timeout-ms:30000}")
    private String requestTimeoutMs;

    @Value("${kafka.producer.delivery-timeout-ms:60000}")
    private String deliveryTimeoutMs;

    public KafkaEventSenderImpl(
            HubEventHandler hubEventHandler,
            SensorEventHandler sensorEventHandler) {
        this.hubEventHandler = hubEventHandler;
        this.sensorEventHandler = sensorEventHandler;

        // Создаем продюсеры один раз при старте
        this.sensorProducer = createProducer();
        this.hubProducer = createProducer();

        System.out.println("✅ Kafka producers initialized");
    }

    private <T> Producer<String, T> createProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class.getName());
        config.put(ProducerConfig.ACKS_CONFIG, acks);
        config.put(ProducerConfig.RETRIES_CONFIG, retries);
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs);

        return new KafkaProducer<>(config);
    }

    @Override
    public boolean send(SensorEvent event) {
        try {
            final SensorEventAvro sensorEventAvro = sensorEventHandler.toAvro(event);
            final ProducerRecord<String, SensorEventAvro> record =
                    new ProducerRecord<>(sensorsTopic, event.getHubId(), sensorEventAvro);

            sensorProducer.send(record).get(5, TimeUnit.SECONDS);

            System.out.println("✅ Sent SensorEvent to Kafka, hubId: " + event.getHubId());
            return true;
        } catch (Exception e) {
            System.err.println("❌ Failed to send SensorEvent: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean send(HubEvent hubEvent) {
        try {
            final HubEventAvro hubEventAvro = hubEventHandler.toAvro(hubEvent);
            final ProducerRecord<String, HubEventAvro> record =
                    new ProducerRecord<>(hubsTopic, hubEvent.getHubId(), hubEventAvro);

            hubProducer.send(record).get(5, TimeUnit.SECONDS);

            System.out.println("✅ Sent HubEvent to Kafka, hubId: " + hubEvent.getHubId());
            return true;
        } catch (Exception e) {
            System.err.println("❌ Failed to send HubEvent: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void destroy() {
        if (sensorProducer != null) {
            sensorProducer.close();
            System.out.println("✅ Sensor producer closed");
        }
        if (hubProducer != null) {
            hubProducer.close();
            System.out.println("✅ Hub producer closed");
        }
    }
}