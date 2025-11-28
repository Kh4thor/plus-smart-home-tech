package ru.yandex.practicum.telemetry.collector.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.scenario.*;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.events.*;

import java.util.List;

@Component
public class ScenarioEventHandler {

    public ScenarioEventAvro toAvro(ScenarioEvent scenarioEvent) {

        ScenarioEventAvro base = ScenarioEventAvro.newBuilder()
                .setHubId(scenarioEvent.getHubId())
                .setTimestamp(scenarioEvent.getTimestamp())
                .setName(scenarioEvent.getName())
                .build();

        HubEventType scenarioEventType = scenarioEvent.getType();
        switch (scenarioEventType) {
            case SCENARIO_ADDED -> {
                ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) scenarioEvent;

                List<DeviceActionAvro> deviceActionAvroList = ((ScenarioAddedEvent) scenarioEvent).getActions()
                        .stream()
                        .map(this::toAvro)
                        .toList();

                List<ScenarioConditionAvro> scenarioConditionAvroList = ((ScenarioAddedEvent) scenarioEvent)
                        .getConditions().stream()
                        .map(this::toAvro)
                        .toList();

                ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                        .setActions(deviceActionAvroList)
                        .setConditions(scenarioConditionAvroList)
                        .build();

                base.setPayload(payload);
                return base;
            }
            case SCENARIO_REMOVED -> {
                return base;
            }
            default -> throw new IllegalArgumentException("Unknown scenario condition operation: " + scenarioEventType);
        }
    }

    private ScenarioConditionAvro toAvro(ScenarioCondition scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(toAvro(scenarioCondition.getType()))
                .setOperation(toAvro(scenarioCondition.getOperation()))
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private ScenarioConditionOperationAvro toAvro(ScenarioConditionOperation scenarioConditionOperation) {
        switch (scenarioConditionOperation) {
            case EQUALS -> {
                return ScenarioConditionOperationAvro.EQUALS;
            }
            case GREATER_THAN -> {
                return ScenarioConditionOperationAvro.GREATER_THAN;
            }
            case LOWER_THAN -> {
                return ScenarioConditionOperationAvro.LOWER_THAN;
            }
            default ->
                    throw new IllegalArgumentException("Unknown scenario condition operation: " + scenarioConditionOperation);
        }
    }

    private ScenarioConditionTypeAvro toAvro(ScenarioConditionType scenarioConditionType) {
        switch (scenarioConditionType) {
            case CO2LEVEL -> {
                return ScenarioConditionTypeAvro.CO2LEVEL;
            }
            case HUMIDITY -> {
                return ScenarioConditionTypeAvro.HUMIDITY;
            }
            case MOTION -> {
                return ScenarioConditionTypeAvro.MOTION;
            }
            case SWITCH -> {
                return ScenarioConditionTypeAvro.SWITCH;
            }
            case LUMINOSITY -> {
                return ScenarioConditionTypeAvro.LUMINOSITY;
            }
            case TEMPERATURE -> {
                return ScenarioConditionTypeAvro.TEMPERATURE;
            }
            default -> throw new IllegalArgumentException("Unknown scenario condition type: " + scenarioConditionType);
        }
    }

    private DeviceActionAvro toAvro(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(toAvro(deviceAction.getType()))
                .setValue(deviceAction.getValue())
                .build();
    }

    private DeviceActionTypeAvro toAvro(DeviceActionType deviceActionType) {
        switch (deviceActionType) {
            case ACTIVATE -> {
                return DeviceActionTypeAvro.ACTIVATE;
            }
            case DEACTIVATE -> {
                return DeviceActionTypeAvro.DEACTIVATE;
            }
            case INVERSE -> {
                return DeviceActionTypeAvro.INVERSE;
            }
            case SET_VALUE -> {
                return DeviceActionTypeAvro.SET_VALUE;
            }
            default -> throw new IllegalArgumentException("Unknown device action type: " + deviceActionType);
        }
    }

    private HubEventType toAvro(HubEventType scenarioEventType) {
        switch (scenarioEventType) {
            case SCENARIO_ADDED -> {
                return HubEventType.SCENARIO_ADDED;
            }
            case SCENARIO_REMOVED -> {
                return HubEventType.SCENARIO_REMOVED;
            }
            default -> throw new IllegalArgumentException("Unknown scenario event type: " + scenarioEventType);
        }
    }
}
