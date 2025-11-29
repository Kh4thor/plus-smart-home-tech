package ru.yandex.practicum.telemetry.collector.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.events.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.events.DeviceType;

@Component
public class DeviceEventHandler {

    public DeviceEventAvro toAvro(DeviceEvent deviceEvent) {

        if (deviceEvent == null) {
            throw new IllegalArgumentException("DeviceEvent cannot be null");
        }

        HubEventType deviceEventType = deviceEvent.getType();

        if (deviceEventType == null) {
            throw new IllegalArgumentException("deviceEvent type is null");
        }

        switch (deviceEventType) {
            case DEVICE_ADDED -> {
                DeviceAddedEvent deviceAddedEventAvro = (DeviceAddedEvent) deviceEvent;
                DeviceAddedEventAvro payload = DeviceAddedEventAvro.newBuilder()
                        .setDeviceType(toAvro(deviceAddedEventAvro.getDeviceType()))
                        .build();
                return setPayload(deviceEvent, payload);
            }
            case DEVICE_REMOVED -> {
                DeviceRemovedEventAvro payload = DeviceRemovedEventAvro.newBuilder().build();
                return setPayload(deviceEvent, payload);
            }
            default -> throw new IllegalArgumentException("Unknown device event type: " + deviceEventType);
        }
    }

    private DeviceTypeAvro toAvro(DeviceType deviceType) {
        if (deviceType == null) {
            throw new IllegalArgumentException("deviceType is null");
        }
        return switch (deviceType) {
            case CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
            case TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
            default -> throw new IllegalArgumentException("Unknown device type: " + deviceType);
        };
    }

    private DeviceEventAvro setPayload(DeviceEvent deviceEvent, SpecificRecordBase payload) {
        return DeviceEventAvro.newBuilder()
                .setId(deviceEvent.getId())
                .setHubId(deviceEvent.getHubId())
                .setTimestamp(deviceEvent.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
