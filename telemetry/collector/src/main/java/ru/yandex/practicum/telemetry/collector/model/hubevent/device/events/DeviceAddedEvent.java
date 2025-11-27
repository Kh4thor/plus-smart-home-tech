package ru.yandex.practicum.telemetry.collector.model.hubevent.device.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.DeviceEvent;

@Getter
@SuperBuilder
public class DeviceAddedEvent extends DeviceEvent {

    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return null;
    }
}