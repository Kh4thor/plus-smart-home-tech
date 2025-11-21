package ru.yandex.practicum.telemetry.collector.model.scenario;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.exceptions.ErrorEventType;
import ru.yandex.practicum.telemetry.collector.model.scenario.events.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.scenario.events.ScenarioRemovedEvent;

@Getter
@ToString
@SuperBuilder

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = ErrorEventType.class
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED"),
})

public abstract class ScenarioEvent {

    @NotNull
    private String hubId;

    private String timestamp;

    @Size(min = 3)
    private String name;

    private ScenarioEventType type;
}
