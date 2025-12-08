package ru.yandex.practicum.model.hubevent.scenario.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeviceAction {

    private String sensorId;
    private DeviceActionType type;
    private Integer value;
}
