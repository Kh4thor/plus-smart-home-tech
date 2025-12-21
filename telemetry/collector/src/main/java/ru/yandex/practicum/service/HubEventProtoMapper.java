package ru.yandex.practicum.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;

@Component
public class HubEventProtoMapper {

    public HubEventAvro toAvro(HubEventProto hubEventProto) {
        SpecificRecordBase payload = null;

        HubEventAvro.Builder hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(hubEventProto.getHubId())
                .setTimestamp(Instant.now());

        switch (hubEventProto.getPayloadCase()) {
            case DEVICE_ADDED_EVENT -> {
                DeviceAddedEventProto eventProto = hubEventProto.getDeviceAddedEvent();
                payload = DeviceAddedEventAvro.newBuilder()
                        .setId(eventProto.getId())
                        .setType(toAvro(eventProto.getType()))
                        .build();
            }

            case DEVICE_REMOVED_EVENT -> {
                DeviceRemovedEventProto eventProto = hubEventProto.getDeviceRemovedEvent();
                payload = DeviceRemovedEventAvro.newBuilder()
                        .setId(eventProto.getId())
                        .build();
            }

            case SCENARIO_ADDED -> {
                ScenarioAddedEventProto eventProto = hubEventProto.getScenarioAdded();
                List<DeviceActionAvro> actionsAvros = eventProto.getActionsList().stream()
                        .map(this::toAvro)
                        .toList();

                List<ScenarioConditionAvro> conditionAvros = eventProto.getConditionsList().stream()
                        .map(this::toAvro)
                        .toList();

                payload = ScenarioAddedEventAvro.newBuilder()
                        .setName(eventProto.getName())
                        .setActions(actionsAvros)
                        .setConditions(conditionAvros)
                        .build();
            }

            case SCENARIO_REMOVED -> {
                ScenarioRemovedEventProto eventProto = hubEventProto.getScenarioRemoved();
                payload = ScenarioRemovedEventAvro.newBuilder()
                        .setName(eventProto.getName())
                        .build();
            }
        }
        if (payload == null) {
            throw new IllegalArgumentException("Unknown hub event proto payload case: " + hubEventProto.getPayloadCase());
        }
        return hubEventAvro.setPayload(payload).build();
    }


    private ScenarioConditionAvro toAvro(ScenarioConditionProto scenarioConditionProto) {
        return ScenarioConditionAvro.newBuilder()
                .setOperation(toAvro(scenarioConditionProto.getOperation()))
                .setType(toAvro(scenarioConditionProto.getType()))
                .setSensorId(scenarioConditionProto.getSensorId())
                .setValue(scenarioConditionProto.getValue())
                .build();
    }

    private ConditionTypeAvro toAvro(ConditionTypeProto conditionTypeProto) {
        return ConditionTypeAvro.valueOf(conditionTypeProto.name());
    }

    private ConditionOperationAvro toAvro(ConditionOperationProto conditionOperationProto) {
        return ConditionOperationAvro.valueOf(conditionOperationProto.name());
    }

    private ActionTypeAvro toAvro(ActionTypeProto actionTypeProto) {
        return ActionTypeAvro.valueOf(actionTypeProto.name());
    }

    private DeviceActionAvro toAvro(DeviceActionProto deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setType(toAvro(deviceAction.getType()))
                .setSensorId(deviceAction.getSensorId())
                .setValue(deviceAction.getValue())
                .build();
    }

    private DeviceTypeAvro toAvro(DeviceTypeProto deviceType) {
        return DeviceTypeAvro.valueOf(deviceType.name());
    }
}
