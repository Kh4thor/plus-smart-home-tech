package ru.yandex.practicum.telemetry.collector.service.handler.device;

import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventAvro;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEventType;

public interface BaseDeviceEventHandler {

    DeviceEventType getMessageType();

    DeviceEventAvro toAvro(DeviceEvent deviceEvent);

    DeviceEventAvro handle(DeviceEvent deviceEvent);
}
