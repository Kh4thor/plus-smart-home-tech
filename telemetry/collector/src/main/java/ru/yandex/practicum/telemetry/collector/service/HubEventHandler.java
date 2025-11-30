package ru.yandex.practicum.telemetry.collector.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.events.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.events.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.events.DeviceType;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.events.*;

import java.util.List;

@Component
public class HubEventHandler {

    public HubEventAvro toAvro(HubEvent hubEvent) {

        SpecificRecordBase payload = null;

        HubEventAvro.Builder hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp());

        switch (hubEvent.getType()) {
            case DEVICE_ADDED -> {
                DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) hubEvent;
                payload = DeviceAddedEventAvro.newBuilder()
                        .setId(deviceAddedEvent.getId())
                        .setType(toAvro(deviceAddedEvent.getDeviceType()))
                        .build();
            }
            case DEVICE_REMOVED -> {
                DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) hubEvent;
                payload = DeviceRemovedEventAvro.newBuilder()
                        .setId(deviceRemovedEvent.getId())
                        .build();
            }
            case SCENARIO_ADDED -> {
                ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) hubEvent;
                List<DeviceActionAvro> actionsAvros = scenarioAddedEvent.getActions().stream()
                        .map(this::toAvro)
                        .toList();

                List<ScenarioConditionAvro> conditionAvros = scenarioAddedEvent.getConditions().stream()
                        .map(this::toAvro)
                        .toList();

                payload = ScenarioAddedEventAvro.newBuilder()
                        .setName(scenarioAddedEvent.getName())
                        .setActions(actionsAvros)
                        .setConditions(conditionAvros)
                        .build();
            }
            case SCENARIO_REMOVED -> {
                ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) hubEvent;
                payload = ScenarioRemovedEventAvro.newBuilder()
                        .setName(scenarioRemovedEvent.getName())
                        .build();
            }
        }

        if (payload == null){
            throw new IllegalArgumentException("Unknown hub event type: " + hubEvent.getType());
        }
        return hubEventAvro.setPayload(payload).build();
    }

    private ScenarioConditionAvro toAvro(ScenarioCondition scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setOperation(toAvro(scenarioCondition.getOperation()))
                .setType(toAvro(scenarioCondition.getType()))
                .setSensorId(scenarioCondition.getSensorId())
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private ConditionTypeAvro toAvro(ScenarioConditionType scenarioConditionType) {
        return switch (scenarioConditionType) {
            case MOTION -> ConditionTypeAvro.MOTION;
            case LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case SWITCH -> ConditionTypeAvro.SWITCH;
            case HUMIDITY -> ConditionTypeAvro.HUMIDITY;
            case TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
        };
    }

    private ConditionOperationAvro toAvro(ScenarioConditionOperation scenarioConditionOperation) {
        return switch (scenarioConditionOperation) {
            case EQUALS -> ConditionOperationAvro.EQUALS;
            case LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
            case GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
        };
    }

    private ActionTypeAvro toAvro(DeviceActionType deviceActionType) {
        return switch (deviceActionType) {
            case ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case INVERSE -> ActionTypeAvro.INVERSE;
            case SET_VALUE -> ActionTypeAvro.SET_VALUE;
        };
    }

    private DeviceActionAvro toAvro(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setType(toAvro(deviceAction.getType()))
                .setSensorId(deviceAction.getSensorId())
                .setValue(deviceAction.getValue())
                .build();
    }

    private DeviceTypeAvro toAvro(DeviceType deviceType) {
        return switch (deviceType) {
            case MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
            case LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
        };
    }
}
