package ru.yandex.practicum.telemetry.collector.model.hubevent;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@SuperBuilder
public abstract class HubEvent {

    @NotNull
    private String hubId;
    private Instant timestamp = Instant.now();

    public abstract HubEventType getType();
}
