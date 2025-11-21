package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;

public abstract class BaseSensorEventHandler {

    public abstract SensorEventType getMessageType();

    protected abstract SensorEventTypeAvro toAvro(SensorEventType deviceEvent);
}
