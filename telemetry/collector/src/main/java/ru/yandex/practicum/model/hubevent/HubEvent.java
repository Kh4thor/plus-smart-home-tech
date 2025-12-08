package ru.yandex.practicum.model.hubevent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.model.hubevent.device.DeviceEvent;
import ru.yandex.practicum.model.hubevent.scenario.ScenarioEvent;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioEvent.class, name = "SCENARIO_REMOVED")
})

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class HubEvent {

    @NotNull
    private String hubId;

    @Builder.Default
    private Instant timestamp = Instant.now();

    public abstract HubEventType getType();
}
