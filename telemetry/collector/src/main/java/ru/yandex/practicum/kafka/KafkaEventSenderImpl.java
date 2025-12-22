package ru.yandex.practicum.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.yandex.practicum.service.HubEventProtoMapper;
import ru.yandex.practicum.service.SensorEventProtoMapper;

import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventSenderImpl implements KafkaEventSender, DisposableBean {

    @Value("${kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${kafka.topic.sensors:telemetry.sensors.v1}")
    private String sensorsTopic;

    @Value("${kafka.topic.hubs:telemetry.hubs.v1}")
    private String hubsTopic;

    private final SensorEventProtoMapper sensorEventProtoMapper;
    private final HubEventProtoMapper hubEventProtoMapper;

    private Producer<String, SensorEventAvro> sensorProducer;
    private Producer<String, HubEventAvro> hubProducer;

    @PostConstruct
    public void init() {
        log.info("Initializing Kafka producers with bootstrap servers: {}", bootstrapServers);

        this.sensorProducer = createProducer();
        this.hubProducer = createProducer();

        log.info("Kafka producers initialized successfully");
    }

    private <T> Producer<String, T> createProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class.getName());
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.RETRIES_CONFIG, 3);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new KafkaProducer<>(config);
    }

    @Override
    public void send(SensorEventProto sensorEventProto) {
        try {
            final SensorEventAvro sensorEventAvro = sensorEventProtoMapper.toAvro(sensorEventProto);
            final ProducerRecord<String, SensorEventAvro> record =
                    new ProducerRecord<>(sensorsTopic, sensorEventProto.getHubId(), sensorEventAvro);

            sensorProducer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Failed to send SensorEvent to Kafka, hubId: {}", sensorEventProto.getHubId(), exception);
                } else {
                    log.info("Sent SensorEvent to Kafka, hubId: {}, partition: {}, offset: {}",
                            sensorEventProto.getHubId(), metadata.partition(), metadata.offset());
                }
            });

        } catch (Exception e) {
            log.error("Failed to process SensorEvent for hubId: {}", sensorEventProto.getHubId(), e);
            throw new RuntimeException("Failed to send sensor event to Kafka", e);
        }
    }

    @Override
    public void send(HubEventProto hubEventProto) {
        try {
            final HubEventAvro hubEventAvro = hubEventProtoMapper.toAvro(hubEventProto);
            final ProducerRecord<String, HubEventAvro> record =
                    new ProducerRecord<>(hubsTopic, hubEventProto.getHubId(), hubEventAvro);

            hubProducer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Failed to send HubEvent to Kafka, hubId: {}", hubEventProto.getHubId(), exception);
                } else {
                    log.info("Sent HubEvent to Kafka, hubId: {}, partition: {}, offset: {}",
                            hubEventProto.getHubId(), metadata.partition(), metadata.offset());
                }
            });

        } catch (Exception e) {
            log.error("Failed to process HubEvent for hubId: {}", hubEventProto.getHubId(), e);
            throw new RuntimeException("Failed to send hub event to Kafka", e);
        }
    }

    @Override
    public void destroy() {
        if (sensorProducer != null) {
            sensorProducer.close();
            log.info("Sensor producer closed");
        }
        if (hubProducer != null) {
            hubProducer.close();
            log.info("Hub producer closed");
        }
    }
}