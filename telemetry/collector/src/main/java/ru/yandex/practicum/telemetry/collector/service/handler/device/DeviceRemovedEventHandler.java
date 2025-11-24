package ru.yandex.practicum.telemetry.collector.service.handler.device;

import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventTypeAvro;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEventType;

public class DeviceRemovedEventHandler implements BaseDeviceEventHandler {

    @Override
    public DeviceEventType getMessageType() {
        return DeviceEventType.DEVICE_REMOVED;
    }

    @Override
    public DeviceEventAvro toAvro(DeviceEvent deviceEvent) {

        return DeviceEventAvro.newBuilder()
                .setId(deviceEvent.getId())
                .setType(DeviceEventTypeAvro.DEVICE_REMOVED)
                .setTimestamp(deviceEvent.getTimestamp())
                .setHubId(deviceEvent.getHubId())
                .build();
    }
}
