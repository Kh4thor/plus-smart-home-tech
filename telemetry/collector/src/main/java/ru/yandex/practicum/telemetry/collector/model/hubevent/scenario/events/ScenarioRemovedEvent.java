package ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEvent;

@Getter
@SuperBuilder
public class ScenarioRemovedEvent extends ScenarioEvent {

    @Override
    public HubEventType getType(){
        return HubEventType.SCENARIO_REMOVED;
    }
}
