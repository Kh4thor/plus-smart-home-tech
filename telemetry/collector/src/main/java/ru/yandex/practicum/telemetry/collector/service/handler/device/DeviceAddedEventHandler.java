package ru.yandex.practicum.telemetry.collector.service.handler.device;

import ru.yandex.practicum.telemetry.collector.model.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEventType;

public class DeviceAddedEventHandler extends BaseDeviceEventHandler {

    @Override
    public DeviceEventType getMessageType() {
        return DeviceEventType.DEVICE_ADDED;
    }

    @Override
    protected DeviceAddedEventAvro toAvro(DeviceEvent deviceEvent) {
        return null;
    }
}
