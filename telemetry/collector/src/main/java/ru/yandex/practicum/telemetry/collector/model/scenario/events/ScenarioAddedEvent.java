package ru.yandex.practicum.telemetry.collector.model.scenario.events;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.model.scenario.ScenarioEvent;
import ru.yandex.practicum.telemetry.collector.model.scenario.ScenarioEventType;

import java.util.List;

@Getter
@SuperBuilder
public class ScenarioAddedEvent extends ScenarioEvent {

    @NotEmpty
    private List<ScenarioCondition> conditions;

    @NotEmpty
    private List<DeviceAction> actions;

    @Override
    public ScenarioEventType getType() {
        return ScenarioEventType.SCENARIO_ADDED;
    }
}



