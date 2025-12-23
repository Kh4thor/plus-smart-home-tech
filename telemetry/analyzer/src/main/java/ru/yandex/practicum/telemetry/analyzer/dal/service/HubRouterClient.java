package ru.yandex.practicum.telemetry.analyzer.dal.service;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.telemetry.analyzer.dal.model.Action;
import ru.yandex.practicum.telemetry.analyzer.dal.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.model.ScenarioAction;
import ru.yandex.practicum.telemetry.analyzer.dal.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.dal.model.enums.DeviceActionType;

import java.time.Instant;

@Slf4j
@Service
public class HubRouterClient {

    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouter;

    public HubRouterClient(@GrpcClient("hub-router") HubRouterControllerGrpc.HubRouterControllerBlockingStub hub) {

        this.hubRouter = hub;
    }

    public void sendAction(ScenarioAction scenarioAction) {
        DeviceActionRequest actionRequest = toActionRequest(scenarioAction);
        hubRouter.handleDeviceAction(actionRequest);
    }

    private Timestamp currentTimestamp() {
        Instant instant = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    private DeviceActionRequest toActionRequest(ScenarioAction scenarioAction) {
        Scenario scenario = scenarioAction.getScenario();
        Sensor sensor = scenarioAction.getSensor();
        Action action = scenarioAction.getAction();

        DeviceActionProto deviceActionProto = DeviceActionProto.newBuilder()
                .setSensorId(sensor.getId())
                .setType(toProto(action.getType()))
                .setValue(action.getValue())
                .build();

        return DeviceActionRequest.newBuilder()
                .setHubId(scenario.getHubId())
                .setScenarioName(scenario.getName())
                .setAction(deviceActionProto)
                .setTimestamp(currentTimestamp())
                .build();
    }

    private ActionTypeProto toProto(DeviceActionType actionType) {
        return ActionTypeProto.valueOf(actionType.name());
    }
}