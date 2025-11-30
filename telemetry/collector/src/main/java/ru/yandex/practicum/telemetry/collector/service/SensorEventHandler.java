package ru.yandex.practicum.telemetry.collector.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.events.*;

@Component
public class SensorEventHandler {

    public SensorEventAvro toAvro(SensorEvent sensorEvent) {

        SpecificRecordBase payload = null;

        SensorEventAvro.Builder sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp());

        switch (sensorEvent.getType()) {
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) sensorEvent;
                payload = ClimateSensorAvro.newBuilder()
                        .setCo2Level(climateSensorEvent.getCo2Level())
                        .setHumidity(climateSensorEvent.getHumidity())
                        .setTemperatureC(climateSensorEvent.getTemperatureC())
                        .build();
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) sensorEvent;
                payload = TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                        .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                        .build();
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorEvent lightSensorEvent = (LightSensorEvent) sensorEvent;
                payload = LightSensorAvro.newBuilder()
                        .setLinkQuality(lightSensorEvent.getLinkQuality())
                        .setLuminosity(lightSensorEvent.getLuminosity())
                        .build();
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorEvent motionSensorEvent = (MotionSensorEvent) sensorEvent;
                payload = MotionSensorAvro.newBuilder()
                        .setMotion(motionSensorEvent.getMotion())
                        .setVoltage(motionSensorEvent.getVoltage())
                        .setLinkQuality(motionSensorEvent.getLinkQuality())
                        .build();
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) sensorEvent;
                payload = SwitchSensorAvro.newBuilder()
                        .setStat(switchSensorEvent.isState())
                        .build();
            }
        }

        if (payload == null) {
            throw new IllegalArgumentException("Unknown sensor event type");
        }

        return sensorEventAvro.setPayload(payload).build();
    }
}
