package ru.yandex.practicum.telemetry.collector.service;

import ru.yandex.practicum.kafka.telemetry.event.device.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.events.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.events.DeviceType;

public class DeviceEventHandler {

    public DeviceEventAvro toAvro(DeviceEvent deviceEvent) {

        DeviceEventAvro base = DeviceEventAvro.newBuilder()
                .setId(deviceEvent.getId())
                .setHubId(deviceEvent.getHubId())
                .setTimestamp(deviceEvent.getTimestamp())
                .setType(toAvro(deviceEvent.getType()))
                .build();

        HubEventType deviceEventType = deviceEvent.getType();
        switch (deviceEventType) {
            case DEVICE_ADDED -> {
                DeviceAddedEvent deviceAddedEventAvro = (DeviceAddedEvent) deviceEvent;
                DeviceAddedEventAvro payload = DeviceAddedEventAvro.newBuilder()
                        .setDeviceType(toAvro(deviceAddedEventAvro.getDeviceType()))
                        .build();
                base.setPayload(payload);
                return base;

            }
            case DEVICE_REMOVED -> {
                return base;
            }
            default -> throw new IllegalArgumentException("Unknown device event type: " + deviceEventType);
        }
    }

    private DeviceEventTypeAvro toAvro(HubEventType deviceEventType) {
        switch (deviceEventType) {
            case DEVICE_ADDED -> {
                return DeviceEventTypeAvro.DEVICE_ADDED;
            }
            case DEVICE_REMOVED -> {
                return DeviceEventTypeAvro.DEVICE_REMOVED;
            }
            default -> throw new IllegalArgumentException("Unknown device event type: " + deviceEventType);
        }
    }

    private DeviceTypeAvro toAvro(DeviceType deviceType) {

        switch (deviceType) {
            case CLIMATE_SENSOR -> {
                return DeviceTypeAvro.CLIMATE_SENSOR;
            }
            case MOTION_SENSOR -> {
                return DeviceTypeAvro.MOTION_SENSOR;
            }
            case LIGHT_SENSOR -> {
                return DeviceTypeAvro.LIGHT_SENSOR;
            }
            case SWITCH_SENSOR -> {
                return DeviceTypeAvro.SWITCH_SENSOR;
            }
            case TEMPERATURE_SENSOR -> {
                return DeviceTypeAvro.TEMPERATURE_SENSOR;
            }
            default -> throw new IllegalArgumentException("Unknown device type: " + deviceType);
        }
    }
}
