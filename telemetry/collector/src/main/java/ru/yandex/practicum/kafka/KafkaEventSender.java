package ru.yandex.practicum.kafka;


import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface KafkaEventSender {

    boolean send(SensorEventProto sensorEventProto);

    boolean send(HubEventProto sensorEventProto);
}