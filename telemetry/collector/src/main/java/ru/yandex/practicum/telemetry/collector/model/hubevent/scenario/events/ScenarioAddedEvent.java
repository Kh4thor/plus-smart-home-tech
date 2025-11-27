package ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.events;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEvent;

import java.util.List;

@Getter
@SuperBuilder
public class ScenarioAddedEvent extends ScenarioEvent {

    @NotEmpty
    private List<ScenarioCondition> conditions;

    @NotEmpty
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}



