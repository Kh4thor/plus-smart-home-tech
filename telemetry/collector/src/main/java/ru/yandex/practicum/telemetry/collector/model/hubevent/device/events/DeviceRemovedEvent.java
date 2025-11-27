package ru.yandex.practicum.telemetry.collector.model.hubevent.device.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.DeviceEvent;

@Getter
@SuperBuilder
public class DeviceRemovedEvent extends DeviceEvent {

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
