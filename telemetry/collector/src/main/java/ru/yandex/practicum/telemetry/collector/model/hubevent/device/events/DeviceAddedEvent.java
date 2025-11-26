package ru.yandex.practicum.telemetry.collector.model.hubevent.device.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.DeviceEventType;

@Getter
@SuperBuilder
public class DeviceAddedEvent extends DeviceEvent {

    private DeviceType deviceType;

    @Override
    public DeviceEventType getType() {
        return DeviceEventType.DEVICE_ADDED;
    }
}