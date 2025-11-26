package ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.events;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEventType;

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



