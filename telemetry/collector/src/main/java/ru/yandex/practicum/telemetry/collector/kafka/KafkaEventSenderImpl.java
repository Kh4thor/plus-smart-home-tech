package ru.yandex.practicum.telemetry.collector.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.SensorEventHandler;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaEventSenderImpl implements KafkaEventSender, DisposableBean {
    private final HubEventHandler hubEventHandler;
    private final SensorEventHandler sensorEventHandler;
    private final Producer<String, SensorEventAvro> sensorProducer;
    private final Producer<String, HubEventAvro> hubProducer;

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
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.RETRIES_CONFIG, 3);
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000);

        return new KafkaProducer<>(config);
    }

    @Override
    public boolean send(SensorEvent event) {
        try {
            final SensorEventAvro sensorEventAvro = sensorEventHandler.toAvro(event);
            final ProducerRecord<String, SensorEventAvro> record =
                    new ProducerRecord<>("telemetry.sensors.v1", event.getHubId(), sensorEventAvro);

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
                    new ProducerRecord<>("telemetry.hubs.v1", hubEvent.getHubId(), hubEventAvro);

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