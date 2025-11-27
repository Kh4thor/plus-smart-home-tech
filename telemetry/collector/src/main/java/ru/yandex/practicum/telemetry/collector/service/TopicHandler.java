package ru.yandex.practicum.telemetry.collector.service;

import ru.yandex.practicum.telemetry.collector.kafka.EventTopic;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

public class TopicHandler {

    public String getTopic(HubEvent hubEvent) {
        String eventClass = hubEvent.getClass().getName();
        throw new IllegalArgumentException("Unknown event class: " + eventClass);
    }

    public String getTopic(SensorEvent sensorEvent) {
        return EventTopic.SENSOR_EVENTS;
    }
}