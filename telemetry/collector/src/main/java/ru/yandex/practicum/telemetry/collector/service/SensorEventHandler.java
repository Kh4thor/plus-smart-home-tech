package ru.yandex.practicum.telemetry.collector.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.sensor.*;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.events.*;

@Component
public class SensorEventHandler {

    public SensorEventAvro toAvro(SensorEvent sensorEvent) {

        SensorEventAvro base = SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setType(toAvro(sensorEvent.getType()))
                .build();

        SensorEventType sensorEventType = sensorEvent.getType();
        switch (sensorEventType) {
            case CLIMATE_SENSOR_EVENT -> {

                ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) sensorEvent;
                ClimateSensorEventAvro payload = ClimateSensorEventAvro.newBuilder()
                        .setCo2Level(climateSensorEvent.getCo2Level())
                        .setHumidity(climateSensorEvent.getHumidity())
                        .setTemperatureC(climateSensorEvent.getTemperatureC())
                        .build();
                base.setPayload(payload);
                return base;
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) sensorEvent;
                TemperatureSensorEventAvro payload = TemperatureSensorEventAvro.newBuilder()
                        .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                        .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                        .build();
                base.setPayload(payload);
                return base;
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorEvent lightSensorEvent = (LightSensorEvent) sensorEvent;
                LightSensorEventAvro payload = LightSensorEventAvro.newBuilder()
                        .setLinkQuality(lightSensorEvent.getLinkQuality())
                        .setLuminosity(lightSensorEvent.getLuminosity())
                        .build();
                base.setPayload(payload);
                return base;
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorEvent motionSensorEvent = (MotionSensorEvent) sensorEvent;
                MotionSensorEventAvro payload = MotionSensorEventAvro.newBuilder()
                        .setMotionDetected(motionSensorEvent.getMotionDetected())
                        .setSensitivity(motionSensorEvent.getSensitivity())
                        .build();
                base.setPayload(payload);
                return base;
            }

            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) sensorEvent;
                SwitchSensorEventAvro payload = SwitchSensorEventAvro.newBuilder()
                        .setState(switchSensorEvent.isState())
                        .build();
                base.setPayload(payload);
                return base;
            }
            default -> throw new IllegalArgumentException("Unknown device type: " + sensorEventType);
        }
    }

    private SensorEventTypeAvro toAvro(SensorEventType sensorEventType) {
        switch (sensorEventType) {
            case MOTION_SENSOR_EVENT -> {
                return SensorEventTypeAvro.MOTION_SENSOR_EVENT;
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                return SensorEventTypeAvro.TEMPERATURE_SENSOR_EVENT;
            }
            case LIGHT_SENSOR_EVENT -> {
                return SensorEventTypeAvro.LIGHT_SENSOR_EVENT;
            }
            case CLIMATE_SENSOR_EVENT -> {
                return SensorEventTypeAvro.CLIMATE_SENSOR_EVENT;
            }
            case SWITCH_SENSOR_EVENT -> {
                return SensorEventTypeAvro.SWITCH_SENSOR_EVENT;
            }
            default -> throw new IllegalArgumentException("Unknown device type: " + sensorEventType);
        }
    }
}
