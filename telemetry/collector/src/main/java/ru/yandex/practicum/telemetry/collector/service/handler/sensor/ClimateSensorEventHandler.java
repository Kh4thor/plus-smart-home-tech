package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import ru.yandex.practicum.telemetry.collector.controller.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;

public class ClimateSensorEventHandler extends BaseSensorEventHandler {

    public ClimateSensorEventHandler(KafkaEventProducer producer) {
//        super(producer);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    protected SensorEventTypeAvro toAvro(SensorEventType deviceEvent) {
        return null;
    }
}
