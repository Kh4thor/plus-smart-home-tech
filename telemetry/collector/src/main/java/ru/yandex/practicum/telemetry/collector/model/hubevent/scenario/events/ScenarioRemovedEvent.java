package ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEventType;

@Getter
@SuperBuilder
public class ScenarioRemovedEvent extends ScenarioEvent {

    @Override
    public ScenarioEventType getType(){
        return ScenarioEventType.SCENARIO_REMOVED;
    }
}
