package ru.yandex.practicum.telemetry.collector.service.handler.device;

import ru.yandex.practicum.telemetry.collector.model.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEventType;

public abstract class BaseDeviceEventHandler {
    public abstract DeviceEventType getMessageType();

    protected abstract DeviceAddedEventAvro toAvro(DeviceEvent deviceEvent);
}
