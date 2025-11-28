package ru.yandex.practicum.telemetry.collector.kafka;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.scenario.ScenarioEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.sensor.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.DeviceEventHandler;
import ru.yandex.practicum.telemetry.collector.service.ScenarioEventHandler;
import ru.yandex.practicum.telemetry.collector.service.SensorEventHandler;

import java.util.Properties;

@Service
public class KafkaEventSenderImpl implements KafkaEventSender {

    private final DeviceEventHandler deviceEventHandler;
    private final ScenarioEventHandler scenarioEventHandler;
    private final SensorEventHandler sensorEventHandler;

    public KafkaEventSenderImpl(DeviceEventHandler deviceEventHandler, ScenarioEventHandler scenarioEventHandler, SensorEventHandler sensorEventHandler) {
        this.deviceEventHandler = deviceEventHandler;
        this.scenarioEventHandler = scenarioEventHandler;
        this.sensorEventHandler = sensorEventHandler;
    }

    @Override
    public boolean send(SensorEvent event) {
        final Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);

        final String topic = "telemetry.sensors.v1";

        try (Producer<String, SensorEventAvro> producer = new KafkaProducer<>(config)) {
            final SensorEventAvro sensorEventAvro = sensorEventHandler.toAvro(event);
            final ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(topic, sensorEventAvro);
            producer.send(record);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка отправки в Kafka", e);
        }
    }

    @Override
    public boolean send(HubEvent event) {
        final Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);

        final String topic = "telemetry.hubs.v1";

        if (event instanceof DeviceEvent deviceEvent) {
            try (Producer<String, DeviceEventAvro> producer = new KafkaProducer<>(config)) {
                DeviceEventAvro deviceEventAvro = deviceEventHandler.toAvro(deviceEvent);
                ProducerRecord<String, DeviceEventAvro> record = new ProducerRecord<>(topic, deviceEventAvro);
                producer.send(record);
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Ошибка отправки в Kafka", e);
            }
        } else if (event instanceof ScenarioEvent scenarioEvent) {
            try (Producer<String, ScenarioEventAvro> producer = new KafkaProducer<>(config)) {
                ScenarioEventAvro scenarioEventAvro = scenarioEventHandler.toAvro(scenarioEvent);
                ProducerRecord<String, ScenarioEventAvro> record = new ProducerRecord<>(topic, scenarioEventAvro);
                producer.send(record);
                return true;
            } catch (Exception e) {
                throw new RuntimeException("Ошибка отправки в Kafka", e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported HubEvent type: " + event.getClass().getSimpleName());
        }
    }
}