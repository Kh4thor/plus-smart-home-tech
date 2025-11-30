package ru.yandex.practicum.kafka;


import ru.yandex.practicum.model.hubevent.HubEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;

public interface KafkaEventSender {

    boolean send(SensorEvent event);

    boolean send(HubEvent event);
}