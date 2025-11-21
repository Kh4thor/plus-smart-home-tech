package ru.yandex.practicum.telemetry.collector.model.device;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.exceptions.ErrorEventType;
import ru.yandex.practicum.telemetry.collector.model.device.events.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.device.events.DeviceRemovedEvent;

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
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
})
public abstract class DeviceEvent {

    @NotNull
    private String id;
    private String hubId;
    private String timestamp;
    private DeviceEventType type;
}



