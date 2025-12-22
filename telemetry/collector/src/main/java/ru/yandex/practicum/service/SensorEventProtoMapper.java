package ru.yandex.practicum.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

@Component
public class SensorEventProtoMapper {

    public SensorEventAvro toAvro(SensorEventProto sensorEventProto) {

        SpecificRecordBase payload = null;

        SensorEventAvro.Builder sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(sensorEventProto.getId())
                .setHubId(sensorEventProto.getHubId())
                .setTimestamp(getTimestamp(sensorEventProto));


        switch (sensorEventProto.getPayloadCase()) {
            case CLIMATE_SENSOR -> {
                ClimateSensorProto sensor = sensorEventProto.getClimateSensor();
                payload = ClimateSensorAvro.newBuilder()
                        .setCo2Level(sensor.getCo2Level())
                        .setHumidity(sensor.getHumidity())
                        .setTemperatureC(sensor.getTemperatureC())
                        .build();

            }
            case TEMPERATURE_SENSOR -> {
                TemperatureSensorProto sensor = sensorEventProto.getTemperatureSensor();
                payload = TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(sensor.getTemperatureC())
                        .setTemperatureF(sensor.getTemperatureF())
                        .build();
            }
            case LIGHT_SENSOR -> {
                LightSensorProto sensor = sensorEventProto.getLightSensor();
                payload = LightSensorAvro.newBuilder()
                        .setLinkQuality(sensor.getLinkQuality())
                        .setLuminosity(sensor.getLuminosity())
                        .build();
            }
            case MOTION_SENSOR -> {
                MotionSensorProto sensor = sensorEventProto.getMotionSensor();
                payload = MotionSensorAvro.newBuilder()
                        .setMotion(sensor.getMotion())
                        .setVoltage(sensor.getVoltage())
                        .setLinkQuality(sensor.getLinkQuality())
                        .build();
            }
            case SWITCH_SENSOR -> {
                SwitchSensorProto sensor = sensorEventProto.getSwitchSensor();
                payload = SwitchSensorAvro.newBuilder()
                        .setStat(sensor.getState())
                        .build();
            }
        }
        if (payload == null) {
            throw new IllegalArgumentException("Unknown sensor event type " + sensorEventProto.getPayloadCase());
        }
        return sensorEventAvro.setPayload(payload).build();
    }


    private Instant getTimestamp(SensorEventProto sensorEventProto) {
        return Instant.ofEpochSecond(
                sensorEventProto.getTimestamp().getSeconds(),
                sensorEventProto.getTimestamp().getNanos()
        );
    }
}
