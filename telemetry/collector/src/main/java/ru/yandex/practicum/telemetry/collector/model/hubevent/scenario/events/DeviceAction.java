package ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.events;

import lombok.Getter;

@Getter
public class DeviceAction {

    private String sensorId;
    private DeviceActionType type;
    private Integer value;
}
