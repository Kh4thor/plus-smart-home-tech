package ru.yandex.practicum.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.events.*;

import java.time.Instant;

@Component
public class SensorEventHandlerProto {

    public SensorEvent toModel(SensorEventProto sensorEventProto) {
        switch (sensorEventProto.getPayloadCase()) {
            case CLIMATE_SENSOR -> {
                ClimateSensorProto climateSensorProto = sensorEventProto.getClimateSensor();
                return ClimateSensorEvent.builder()
                        .id(sensorEventProto.getId())
                        .hubId(sensorEventProto.getHubId())
                        .timestamp(getTimestamp(sensorEventProto))
                        .temperatureC(climateSensorProto.getTemperatureC())
                        .humidity(climateSensorProto.getHumidity())
                        .co2Level(climateSensorProto.getCo2Level())
                        .build();
            }
            case TEMPERATURE_SENSOR -> {
                TemperatureSensorProto temperatureSensorProto = sensorEventProto.getTemperatureSensor();
                return TemperatureSensorEvent.builder()
                        .id(sensorEventProto.getId())
                        .hubId(sensorEventProto.getHubId())
                        .timestamp(getTimestamp(sensorEventProto))
                        .temperatureC(temperatureSensorProto.getTemperatureC())
                        .temperatureF(temperatureSensorProto.getTemperatureF())
                        .build();
            }
            case LIGHT_SENSOR -> {
                LightSensorProto lightSensorProto = sensorEventProto.getLightSensor();
                return LightSensorEvent.builder()
                        .id(sensorEventProto.getId())
                        .hubId(sensorEventProto.getHubId())
                        .timestamp(getTimestamp(sensorEventProto))
                        .linkQuality(lightSensorProto.getLinkQuality())
                        .luminosity(lightSensorProto.getLuminosity())
                        .build();
            }
            case SWITCH_SENSOR -> {
                SwitchSensorProto switchSensorProto = sensorEventProto.getSwitchSensor();
                return SwitchSensorEvent.builder()
                        .id(sensorEventProto.getId())
                        .hubId(sensorEventProto.getHubId())
                        .timestamp(getTimestamp(sensorEventProto))
                        .state(switchSensorProto.getState())
                        .build();
            }
            case MOTION_SENSOR -> {
                MotionSensorProto motionSensorProto = sensorEventProto.getMotionSensor();
                return MotionSensorEvent.builder()
                        .id(sensorEventProto.getId())
                        .hubId(sensorEventProto.getHubId())
                        .timestamp(getTimestamp(sensorEventProto))
                        .linkQuality(motionSensorProto.getLinkQuality())
                        .motion(motionSensorProto.getMotion())
                        .voltage(motionSensorProto.getVoltage())
                        .build();
            }
            default ->
                    throw new IllegalArgumentException("Unknown sensor event type " + sensorEventProto.getPayloadCase());

        }
    }

    private Instant getTimestamp(SensorEventProto sensorEventProto) {

        return Instant.ofEpochSecond(
                sensorEventProto.getTimestamp().getSeconds(),
                sensorEventProto.getTimestamp().getNanos()
        );
    }
}
