package ru.yandex.practicum.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.sensor.SensorEvent;

@Component
public interface SensorEventHandler {
    String getMessageType();
    com.google.protobuf.GeneratedMessageV3 toProto (SensorEvent sensorEvent);
}