package ru.yandex.practicum.telemetry.analyzer.dal.model.enums;

import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

public enum ConditionType {
    MOTION,
    LUMINOSITY,
    SWITCH,
    TEMPERATURE,
    CO2LEVEL,
    HUMIDITY;

    public static ConditionType from(ConditionTypeAvro conditionTypeAvro) {
        for (ConditionType conditionType : ConditionType.values()) {
            if (conditionType.name().equals(conditionTypeAvro.name())) {
                return conditionType;
            }
        }
        throw new IllegalArgumentException("Unable to convert ConditionTypeAvro to ConditionType");
    }
}
