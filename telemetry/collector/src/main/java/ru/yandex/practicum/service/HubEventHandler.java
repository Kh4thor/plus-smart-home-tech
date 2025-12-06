package ru.yandex.practicum.service;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.model.hubevent.HubEvent;
import ru.yandex.practicum.model.hubevent.device.events.DeviceAddedEvent;
import ru.yandex.practicum.model.hubevent.device.events.DeviceRemovedEvent;
import ru.yandex.practicum.model.hubevent.device.events.DeviceType;
import ru.yandex.practicum.model.hubevent.scenario.events.*;

import java.time.Instant;
import java.util.List;

@Component
public class HubEventHandler {

    public HubEventProto toProto(HubEvent hubEvent) {

        Instant instant = hubEvent.getTimestamp();  // java.time.Instant
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();

        HubEventProto.Builder hubEventProto = HubEventProto.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(timestamp);

        switch (hubEvent.getType()) {
            case DEVICE_ADDED -> {
                DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) hubEvent;
                DeviceAddedEventProto deviceAddedEventProto = DeviceAddedEventProto.newBuilder()
                        .setId(deviceAddedEvent.getId())
                        .setType(toProto(deviceAddedEvent.getDeviceType()))
                        .build();
                hubEventProto.setDeviceAddedEvent(deviceAddedEventProto).build();
            }
            case DEVICE_REMOVED -> {
                DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) hubEvent;
                DeviceRemovedEventProto deviceRemovedEventProto = DeviceRemovedEventProto.newBuilder()
                        .setId(deviceRemovedEvent.getId())
                        .build();
                hubEventProto.setDeviceRemovedEvent(deviceRemovedEventProto).build();
            }
            case SCENARIO_ADDED -> {
                ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) hubEvent;
                List<DeviceActionProto> actionsProto = scenarioAddedEvent.getActions().stream()
                        .map(this::toProto)
                        .toList();

                List<ScenarioConditionProto> conditionsProto = scenarioAddedEvent.getConditions().stream()
                        .map(this::toProto)
                        .toList();

                ScenarioAddedEventProto scenarioAddedEventProto = ScenarioAddedEventProto.newBuilder()
                        .setName(scenarioAddedEvent.getName())
                        .addAllActions(actionsProto)
                        .addAllConditions(conditionsProto)
                        .build();
                hubEventProto.setScenarioAdded(scenarioAddedEventProto).build();
            }
            case SCENARIO_REMOVED -> {
                ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) hubEvent;
                ScenarioRemovedEventProto scenarioRemovedEventProto = ScenarioRemovedEventProto.newBuilder()
                        .setName(scenarioRemovedEvent.getName())
                        .build();
                hubEventProto.setScenarioRemoved(scenarioRemovedEventProto);
            }
            default ->  throw new IllegalArgumentException("Unknown hub event type: " + hubEvent.getType());
        }
        return hubEventProto.build();
    }

    private ScenarioConditionProto toProto(ScenarioCondition scenarioCondition) {
        return ScenarioConditionProto.newBuilder()
                .setOperation(toProto(scenarioCondition.getOperation()))
                .setType(toProto(scenarioCondition.getType()))
                .setSensorId(scenarioCondition.getSensorId())
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private ConditionTypeProto toProto(ScenarioConditionType scenarioConditionType) {
        return switch (scenarioConditionType) {
            case MOTION -> ConditionTypeProto.MOTION;
            case LUMINOSITY -> ConditionTypeProto.LUMINOSITY;
            case SWITCH -> ConditionTypeProto.SWITCH;
            case HUMIDITY -> ConditionTypeProto.HUMIDITY;
            case TEMPERATURE -> ConditionTypeProto.TEMPERATURE;
            case CO2LEVEL -> ConditionTypeProto.CO2LEVEL;
        };
    }

    private ConditionOperationProto toProto(ScenarioConditionOperation scenarioConditionOperation) {
        return switch (scenarioConditionOperation) {
            case EQUALS -> ConditionOperationProto.EQUALS;
            case LOWER_THAN -> ConditionOperationProto.LOWER_THAN;
            case GREATER_THAN -> ConditionOperationProto.GREATER_THAN;
        };
    }

    private ActionTypeProto toProto(DeviceActionType deviceActionType) {
        return switch (deviceActionType) {
            case ACTIVATE -> ActionTypeProto.ACTIVATE;
            case DEACTIVATE -> ActionTypeProto.DEACTIVATE;
            case INVERSE -> ActionTypeProto.INVERSE;
            case SET_VALUE -> ActionTypeProto.SET_VALUE;
        };
    }

    private DeviceActionProto toProto(DeviceAction deviceAction) {
        return DeviceActionProto.newBuilder()
                .setType(toProto(deviceAction.getType()))
                .setSensorId(deviceAction.getSensorId())
                .setValue(deviceAction.getValue())
                .build();
    }

    private DeviceTypeProto toProto(DeviceType deviceType) {
        return switch (deviceType) {
            case MOTION_SENSOR -> DeviceTypeProto.MOTION_SENSOR;
            case TEMPERATURE_SENSOR -> DeviceTypeProto.TEMPERATURE_SENSOR;
            case LIGHT_SENSOR -> DeviceTypeProto.LIGHT_SENSOR;
            case CLIMATE_SENSOR -> DeviceTypeProto.CLIMATE_SENSOR;
            case SWITCH_SENSOR -> DeviceTypeProto.SWITCH_SENSOR;
        };
    }
}
