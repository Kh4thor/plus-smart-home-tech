package ru.yandex.practicum.telemetry.analyzer.dal.model;

import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

public enum DeviceActionType {
    ACTIVATE,
    DEACTIVATE,
    INVERSE,
    SET_VALUE;

    public static DeviceActionType from(ActionTypeAvro actionTypeAvro) {
        for (DeviceActionType deviceActionType : DeviceActionType.values()) {
            if (deviceActionType.name().equalsIgnoreCase(actionTypeAvro.name())) {
                return deviceActionType;
            }
        }
        throw new IllegalArgumentException("Unable to convert DeviceActionAvro to DeviceActionType");
    }
}
