package ru.yandex.practicum.telemetry.collector.service.handler.device;

import ru.yandex.practicum.kafka.telemetry.event.device.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEventType;
import ru.yandex.practicum.telemetry.collector.model.device.events.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.device.events.DeviceType;

public class DeviceAddedEventHandler implements BaseDeviceEventHandler {

    @Override
    public DeviceEventType getMessageType() {
        return DeviceEventType.DEVICE_ADDED;
    }

    @Override
    public DeviceEventAvro toAvro(DeviceEvent deviceEvent) {
        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) deviceEvent;

        DeviceAddedEventAvro payload = DeviceAddedEventAvro.newBuilder()
                .setDeviceType(toAvro(deviceAddedEvent.getDeviceType()))
                .build();

        return DeviceEventAvro.newBuilder()
                .setId(deviceEvent.getId())
                .setHubId(deviceEvent.getHubId())
                .setTimestamp(deviceEvent.getTimestamp())
                .setType(DeviceEventTypeAvro.DEVICE_ADDED)
                .setPayload(payload)
                .build();
    }

    @Override
    public DeviceEventAvro handle(DeviceEvent deviceEvent) {
        DeviceEventType type = deviceEvent.getType();
        switch (type) {
            case DEVICE_ADDED -> {
                return DeviceEventAvro.newBuilder()
                        .setId(deviceEvent.getId())
                        .setHubId(deviceEvent.getHubId())
                        .setTimestamp(deviceEvent.getTimestamp())
                        .setType(DeviceEventTypeAvro.DEVICE_ADDED)
                        .build();
            }

            case DEVICE_REMOVED -> {
                return DeviceEventAvro.newBuilder()
                        .setId(deviceEvent.getId())
                        .setHubId(deviceEvent.getHubId())
                        .setTimestamp(deviceEvent.getTimestamp())
                        .setType(DeviceEventTypeAvro.DEVICE_REMOVED)
                        .build();
            }
        }
    }
}

private DeviceTypeAvro toAvro(DeviceType deviceType) {

    switch (deviceType) {
        case DeviceType.CLIMATE_SENSOR -> {
            return DeviceTypeAvro.CLIMATE_SENSOR;
        }
        case DeviceType.MOTION_SENSOR -> {
            return DeviceTypeAvro.MOTION_SENSOR;
        }
        case DeviceType.LIGHT_SENSOR -> {
            return DeviceTypeAvro.LIGHT_SENSOR;
        }
        case DeviceType.SWITCH_SENSOR -> {
            return DeviceTypeAvro.SWITCH_SENSOR;
        }
        case DeviceType.TEMPERATURE_SENSOR -> {
            return DeviceTypeAvro.TEMPERATURE_SENSOR;
        }
        default -> throw new IllegalArgumentException("Unknown deviceType: " + deviceType);
    }
}
}
