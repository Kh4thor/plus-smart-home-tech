package ru.yandex.practicum.telemetry.collector.kafka;


import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

public interface KafkaEventSender {

    boolean send(SensorEvent event);

    boolean send(HubEvent event);
}