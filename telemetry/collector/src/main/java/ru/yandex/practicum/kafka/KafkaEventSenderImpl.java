package ru.yandex.practicum.kafka;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.hubevent.HubEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.service.HubEventHandlerAvro;
import ru.yandex.practicum.service.HubEventHandlerProto;
import ru.yandex.practicum.service.SensorEventHandlerAvro;
import ru.yandex.practicum.service.SensorEventHandlerProto;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaEventSenderImpl implements KafkaEventSender, DisposableBean {
    private final HubEventHandlerAvro hubEventHandlerAvro;
    private final SensorEventHandlerAvro sensorEventHandlerAvro;
    private final HubEventHandlerProto hubEventHandlerProto;
    private final SensorEventHandlerProto sensorEventHandlerProto;

    private Producer<String, SensorEventAvro> sensorProducer;
    private Producer<String, HubEventAvro> hubProducer;

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
            HubEventHandlerAvro hubEventHandlerAvro,
            SensorEventHandlerAvro sensorEventHandlerAvro, HubEventHandlerProto hubEventHandlerProto, SensorEventHandlerProto sensorEventHandlerProto) {
        this.hubEventHandlerAvro = hubEventHandlerAvro;
        this.sensorEventHandlerAvro = sensorEventHandlerAvro;
        this.hubEventHandlerProto = hubEventHandlerProto;
        this.sensorEventHandlerProto = sensorEventHandlerProto;
    }

    @PostConstruct
    public void init() {
        System.out.println("Initializing Kafka producers with: " + bootstrapServers);

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
    public boolean send(SensorEventProto sensorEventProto) {
        try {
            final SensorEvent sensorEvent = sensorEventHandlerProto.toModel(sensorEventProto);
            final SensorEventAvro sensorEventAvro = sensorEventHandlerAvro.toAvro(sensorEvent);
            final ProducerRecord<String, SensorEventAvro> record =
                    new ProducerRecord<>(sensorsTopic, sensorEvent.getHubId(), sensorEventAvro);

            sensorProducer.send(record).get(5, TimeUnit.SECONDS);

            System.out.println("✅ Sent SensorEvent to Kafka, hubId: " + sensorEvent.getHubId());
            return true;
        } catch (Exception e) {
            System.err.println("❌ Failed to send SensorEvent: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean send(HubEventProto hubEventProto) {
        try {
            final HubEvent hubEvent = hubEventHandlerProto.toModel(hubEventProto);
            final HubEventAvro hubEventAvro = hubEventHandlerAvro.toAvro(hubEvent);
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