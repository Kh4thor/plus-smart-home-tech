package ru.yandex.practicum.service;

import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.SensorEventType;
import ru.yandex.practicum.model.sensor.events.MotionSensorEvent;

public class MotionSensorEventHandler implements SensorEventHandler {

    @Override
    public String getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT.toString();
    }

    @Override
    public SensorEventProto.Builder toProto(SensorEvent sensorEvent) {

        MotionSensorEvent motionSensorEvent = (MotionSensorEvent) sensorEvent;

        com.google.protobuf.Timestamp timestamp = com.google.protobuf.Timestamp.newBuilder()
                .setSeconds(motionSensorEvent.getTimestamp().getEpochSecond())
                .setNanos(motionSensorEvent.getTimestamp().getNano())
                .build();

        SensorEventProto.Builder sensorEventProto = SensorEventProto.newBuilder()
                .setId(motionSensorEvent.getId())
                .setHubId(motionSensorEvent.getHubId())
                .setTimestamp(timestamp);

        MotionSensorProto motionSensorProto = MotionSensorProto.newBuilder()
                .setMotion(motionSensorEvent.getMotion())
                .setLinkQuality(motionSensorEvent.getLinkQuality())
                .setVoltage(motionSensorEvent.getVoltage())
                .build();

        sensorEventProto.setMotionSensor(motionSensorProto).build();

        return sensorEventProto;
    }
}
