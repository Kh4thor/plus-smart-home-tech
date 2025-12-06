package ru.yandex.practicum.service;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.events.*;

import java.time.Instant;

@Component
public class SensorEventHandler {

    public SensorEventProto toProto(SensorEvent sensorEvent) {

        Instant instant = sensorEvent.getTimestamp();  // java.time.Instant
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();

        SensorEventProto.Builder sensorEventProto = SensorEventProto.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(timestamp);

        switch (sensorEvent.getType()) {
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) sensorEvent;
                ClimateSensorProto climateSensorProto = ClimateSensorProto.newBuilder()
                        .setCo2Level(climateSensorEvent.getCo2Level())
                        .setHumidity(climateSensorEvent.getHumidity())
                        .setTemperatureC(climateSensorEvent.getTemperatureC())
                        .build();
                sensorEventProto.setClimateSensor(climateSensorProto);
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) sensorEvent;
                TemperatureSensorProto temperatureSensorProto = TemperatureSensorProto.newBuilder()
                        .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                        .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                        .build();
                sensorEventProto.setTemperatureSensor(temperatureSensorProto);
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorEvent lightSensorEvent = (LightSensorEvent) sensorEvent;
                LightSensorProto lightSensorProto = LightSensorProto.newBuilder()
                        .setLinkQuality(lightSensorEvent.getLinkQuality())
                        .setLuminosity(lightSensorEvent.getLuminosity())
                        .build();
                sensorEventProto.setLightSensor(lightSensorProto);
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorEvent motionSensorEvent = (MotionSensorEvent) sensorEvent;
                MotionSensorProto motionSensorProto = MotionSensorProto.newBuilder()
                        .setMotion(motionSensorEvent.getMotion())
                        .setVoltage(motionSensorEvent.getVoltage())
                        .setLinkQuality(motionSensorEvent.getLinkQuality())
                        .build();
                sensorEventProto.setMotionSensor(motionSensorProto);
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) sensorEvent;
                SwitchSensorProto switchSensorProto = SwitchSensorProto.newBuilder()
                        .setState(switchSensorEvent.isState())
                        .build();
                sensorEventProto.setSwitchSensor(switchSensorProto);
            }
            default -> throw new IllegalArgumentException("Unknown sensor event type");
        }
        return sensorEventProto.build();
    }
}