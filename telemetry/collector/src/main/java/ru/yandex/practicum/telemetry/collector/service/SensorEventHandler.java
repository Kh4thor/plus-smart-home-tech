package ru.yandex.practicum.telemetry.collector.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.sensor.*;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.events.*;

@Component
public class SensorEventHandler {

    public SensorEventAvro toAvro(SensorEvent sensorEvent) {

        SensorEventType sensorEventType = sensorEvent.getType();
        switch (sensorEventType) {
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) sensorEvent;
                ClimateSensorEventAvro payload = ClimateSensorEventAvro.newBuilder()
                        .setCo2Level(climateSensorEvent.getCo2Level())
                        .setHumidity(climateSensorEvent.getHumidity())
                        .setTemperatureC(climateSensorEvent.getTemperatureC())
                        .build();
                return setPayload(sensorEvent, payload);
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) sensorEvent;
                TemperatureSensorEventAvro payload = TemperatureSensorEventAvro.newBuilder()
                        .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                        .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                        .build();
                return setPayload(sensorEvent, payload);
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorEvent lightSensorEvent = (LightSensorEvent) sensorEvent;
                LightSensorEventAvro payload = LightSensorEventAvro.newBuilder()
                        .setLinkQuality(lightSensorEvent.getLinkQuality())
                        .setLuminosity(lightSensorEvent.getLuminosity())
                        .build();
                return setPayload(sensorEvent, payload);
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorEvent motionSensorEvent = (MotionSensorEvent) sensorEvent;
                MotionSensorEventAvro payload = MotionSensorEventAvro.newBuilder()
                        .setMotionDetected(motionSensorEvent.getMotionDetected())
                        .setSensitivity(motionSensorEvent.getSensitivity())
                        .build();
                return setPayload(sensorEvent, payload);
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) sensorEvent;
                SwitchSensorEventAvro payload = SwitchSensorEventAvro.newBuilder()
                        .setState(switchSensorEvent.isState())
                        .build();
                return setPayload(sensorEvent, payload);
            }
            default -> throw new IllegalArgumentException("Unknown device type: " + sensorEventType);
        }
    }

    private SensorEventTypeAvro toAvro(SensorEventType sensorEventType) {
        return switch (sensorEventType) {
            case MOTION_SENSOR_EVENT -> SensorEventTypeAvro.MOTION_SENSOR_EVENT;
            case TEMPERATURE_SENSOR_EVENT -> SensorEventTypeAvro.TEMPERATURE_SENSOR_EVENT;
            case LIGHT_SENSOR_EVENT -> SensorEventTypeAvro.LIGHT_SENSOR_EVENT;
            case CLIMATE_SENSOR_EVENT -> SensorEventTypeAvro.CLIMATE_SENSOR_EVENT;
            case SWITCH_SENSOR_EVENT -> SensorEventTypeAvro.SWITCH_SENSOR_EVENT;
            default -> throw new IllegalArgumentException("Unknown device type: " + sensorEventType);
        };
    }

    private SensorEventAvro setPayload(SensorEvent sensorEvent, SpecificRecordBase payload) {
        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setType(toAvro(sensorEvent.getType()))
                .setPayload(payload)
                .build();
    }
}
