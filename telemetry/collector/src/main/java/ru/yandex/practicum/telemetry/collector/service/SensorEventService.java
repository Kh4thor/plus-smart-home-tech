package ru.yandex.practicum.telemetry.collector.service;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.sensor.SensorEventHandler;


@AllArgsConstructor
public class SensorEventService {

    private final SensorEventHandler sensorEventHandler;
    private final KafkaProducer<K,V>

    public void send () {

    }

}
