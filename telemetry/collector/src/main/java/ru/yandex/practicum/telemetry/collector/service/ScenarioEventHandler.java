package ru.yandex.practicum.telemetry.collector.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.scenario.*;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.events.*;

import java.util.List;

@Component
public class ScenarioEventHandler {

    public ScenarioEventAvro toAvro(ScenarioEvent scenarioEvent) {

        if (scenarioEvent == null) {
            throw new IllegalArgumentException("scenarioEvent is null");
        }

        HubEventType scenarioEventType = scenarioEvent.getType();
        switch (scenarioEventType) {

            case SCENARIO_ADDED -> {
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

                return setPayload(scenarioEvent, payload);
            }
            case SCENARIO_REMOVED -> {
                ScenarioEventAvro payload = ScenarioEventAvro.newBuilder().build();
                return setPayload(scenarioEvent, payload);
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
        return switch (scenarioConditionOperation) {
            case EQUALS -> ScenarioConditionOperationAvro.EQUALS;
            case GREATER_THAN -> ScenarioConditionOperationAvro.GREATER_THAN;
            case LOWER_THAN -> ScenarioConditionOperationAvro.LOWER_THAN;
            default -> throw new IllegalArgumentException("Unknown scenario condition operation: "
                    + scenarioConditionOperation);
        };
    }

    private ScenarioConditionTypeAvro toAvro(ScenarioConditionType scenarioConditionType) {
        return switch (scenarioConditionType) {
            case CO2LEVEL -> ScenarioConditionTypeAvro.CO2LEVEL;
            case HUMIDITY -> ScenarioConditionTypeAvro.HUMIDITY;
            case MOTION -> ScenarioConditionTypeAvro.MOTION;
            case SWITCH -> ScenarioConditionTypeAvro.SWITCH;
            case LUMINOSITY -> ScenarioConditionTypeAvro.LUMINOSITY;
            case TEMPERATURE -> ScenarioConditionTypeAvro.TEMPERATURE;
            default -> throw new IllegalArgumentException("Unknown scenario condition type: " + scenarioConditionType);
        };
    }

    private DeviceActionAvro toAvro(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(toAvro(deviceAction.getType()))
                .setValue(deviceAction.getValue())
                .build();
    }

    private DeviceActionTypeAvro toAvro(DeviceActionType deviceActionType) {
        return switch (deviceActionType) {
            case ACTIVATE -> DeviceActionTypeAvro.ACTIVATE;
            case DEACTIVATE -> DeviceActionTypeAvro.DEACTIVATE;
            case INVERSE -> DeviceActionTypeAvro.INVERSE;
            case SET_VALUE -> DeviceActionTypeAvro.SET_VALUE;
            default -> throw new IllegalArgumentException("Unknown device action type: " + deviceActionType);
        };
    }

    private HubEventType toAvro(HubEventType scenarioEventType) {
        return switch (scenarioEventType) {
            case SCENARIO_ADDED -> HubEventType.SCENARIO_ADDED;
            case SCENARIO_REMOVED -> HubEventType.SCENARIO_REMOVED;
            default -> throw new IllegalArgumentException("Unknown scenario event type: " + scenarioEventType);
        };
    }

    private ScenarioEventAvro setPayload(ScenarioEvent scenarioEvent, SpecificRecordBase payload) {
        return ScenarioEventAvro.newBuilder()
                .setHubId(scenarioEvent.getHubId())
                .setTimestamp(scenarioEvent.getTimestamp())
                .setName(scenarioEvent.getName())
                .setPayload(payload)
                .build();
    }
}
