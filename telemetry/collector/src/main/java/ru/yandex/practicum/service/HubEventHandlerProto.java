package ru.yandex.practicum.service;

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
public class HubEventHandlerProto {

    public HubEvent toModel(HubEventProto hubEventProto) {

        switch (hubEventProto.getPayloadCase()) {
            case DEVICE_ADDED_EVENT -> {
                DeviceAddedEventProto deviceAddedEventProto = hubEventProto.getDeviceAddedEvent();

                return DeviceAddedEvent.builder()
                        .id(deviceAddedEventProto.getId())
                        .hubId(hubEventProto.getHubId())
                        .timestamp(getTimestamp(hubEventProto))
                        .deviceType(toModel(deviceAddedEventProto.getType()))
                        .build();
            }

            case DEVICE_REMOVED_EVENT -> {
                DeviceRemovedEventProto deviceRemovedEventProto = hubEventProto.getDeviceRemovedEvent();
                return DeviceRemovedEvent.builder()
                        .id(deviceRemovedEventProto.getId())
                        .hubId(hubEventProto.getHubId())
                        .timestamp(getTimestamp(hubEventProto))
                        .build();
            }

            case SCENARIO_ADDED -> {
                ScenarioAddedEventProto scenarioAddedEventProto = hubEventProto.getScenarioAdded();
                return ScenarioAddedEvent.builder()
                        .hubId(hubEventProto.getHubId())
                        .timestamp(getTimestamp(hubEventProto))
                        .name(scenarioAddedEventProto.getName())
                        .actions(toDeviceActionList(scenarioAddedEventProto.getActionsList()))
                        .conditions(toScenarioConditionList(scenarioAddedEventProto.getConditionsList()))
                        .build();
            }

            case SCENARIO_REMOVED -> {
                ScenarioRemovedEventProto scenarioRemovedEventProto = hubEventProto.getScenarioRemoved();
                return ScenarioRemovedEvent.builder()
                        .hubId(hubEventProto.getHubId())
                        .timestamp(getTimestamp(hubEventProto))
                        .name(scenarioRemovedEventProto.getName())
                        .build();
            }

            default -> throw new IllegalArgumentException("Unknown payload case: " + hubEventProto.getPayloadCase());
        }
    }

    private List<ScenarioCondition> toScenarioConditionList(List<ScenarioConditionProto> scenarioConditionProto) {
        return scenarioConditionProto.stream()
                .map(this::toModel)
                .toList();
    }

    private ScenarioConditionType toModel(ConditionTypeProto conditionTypeProto) {
        return switch (conditionTypeProto) {
            case TEMPERATURE -> ScenarioConditionType.TEMPERATURE;
            case HUMIDITY -> ScenarioConditionType.HUMIDITY;
            case SWITCH -> ScenarioConditionType.SWITCH;
            case MOTION -> ScenarioConditionType.MOTION;
            case CO2LEVEL -> ScenarioConditionType.CO2LEVEL;
            case LUMINOSITY -> ScenarioConditionType.LUMINOSITY;
            default -> throw new IllegalArgumentException("Unknown condition type: " + conditionTypeProto);
        };
    }

    private ScenarioConditionOperation toModel(ConditionOperationProto conditionOperationProto) {
        return switch (conditionOperationProto) {
            case EQUALS -> ScenarioConditionOperation.EQUALS;
            case LOWER_THAN -> ScenarioConditionOperation.LOWER_THAN;
            case GREATER_THAN -> ScenarioConditionOperation.GREATER_THAN;
            default -> throw new IllegalArgumentException("Unknown condition operation: " + conditionOperationProto);
        };
    }

    private ScenarioCondition toModel(ScenarioConditionProto scenarioConditionProto) {
        return ScenarioCondition.builder()
                .sensorId(scenarioConditionProto.getSensorId())
                .type(toModel(scenarioConditionProto.getType()))
                .operation(toModel(scenarioConditionProto.getOperation()))
                .value(scenarioConditionProto.getValue())
                .build();
    }


    private List<DeviceAction> toDeviceActionList(List<DeviceActionProto> actionsProto) {
        return actionsProto.stream()
                .map(this::toModel)
                .toList();
    }


    private DeviceAction toModel(DeviceActionProto deviceActionProto) {
        return DeviceAction.builder()
                .sensorId(deviceActionProto.getSensorId())
                .type(toModel(deviceActionProto.getType()))
                .value(deviceActionProto.getValue())
                .build();
    }

    private DeviceActionType toModel(ActionTypeProto actionTypeProto) {
        return switch (actionTypeProto) {
            case SET_VALUE -> DeviceActionType.SET_VALUE;
            case INVERSE -> DeviceActionType.INVERSE;
            case ACTIVATE -> DeviceActionType.ACTIVATE;
            case DEACTIVATE -> DeviceActionType.DEACTIVATE;
            default -> throw new IllegalArgumentException("Unknown action type: " + actionTypeProto);
        };
    }


    private Instant getTimestamp(HubEventProto hubEventProto) {
        return Instant.ofEpochSecond(
                hubEventProto.getTimestamp().getSeconds(),
                hubEventProto.getTimestamp().getNanos()
        );
    }

    private DeviceType toModel(DeviceTypeProto deviceTypeProto) {
        return switch (deviceTypeProto) {
            case MOTION_SENSOR -> DeviceType.MOTION_SENSOR;
            case LIGHT_SENSOR -> DeviceType.LIGHT_SENSOR;
            case SWITCH_SENSOR -> DeviceType.SWITCH_SENSOR;
            case CLIMATE_SENSOR -> DeviceType.CLIMATE_SENSOR;
            case TEMPERATURE_SENSOR -> DeviceType.TEMPERATURE_SENSOR;
            default -> throw new IllegalArgumentException("Unknown device type: " + deviceTypeProto);
        };
    }
}
