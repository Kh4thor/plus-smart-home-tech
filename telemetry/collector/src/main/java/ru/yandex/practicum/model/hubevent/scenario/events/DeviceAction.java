package ru.yandex.practicum.model.hubevent.scenario.events;

import lombok.Getter;

@Getter
public class DeviceAction {

    private String sensorId;
    private DeviceActionType type;
    private Integer value;
}
