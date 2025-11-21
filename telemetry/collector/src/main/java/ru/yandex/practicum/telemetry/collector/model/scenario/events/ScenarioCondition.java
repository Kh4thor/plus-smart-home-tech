package ru.yandex.practicum.telemetry.collector.model.scenario.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class ScenarioCondition {
    private String description;
    private String sensorId;
    private ScenarioConditionType type;
    private ScenarioConditionOperation operation;
    private Integer value;
}