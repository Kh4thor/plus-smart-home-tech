package ru.yandex.practicum.telemetry.collector.controller;

import ru.yandex.practicum.telemetry.collector.model.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEventType;

public interface DeviceEventHandler {

    DeviceEventType getMessageType();

    void handle(DeviceEvent deviceEvent);
}
