package ru.yandex.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.KafkaEventSender;
import telemetry.service.collector.CollectorControllerGrpc;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final KafkaEventSender kafkaEventSender;

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        String hubId = request.getHubId();
        String payloadCase = request.getPayloadCase().name();

        log.info("📥 [gRPC] Received HubEvent - hubId: {}, payload: {}", hubId, payloadCase);

        try {
            // Детальная отладка для SCENARIO_ADDED
            if (request.getPayloadCase() == HubEventProto.PayloadCase.SCENARIO_ADDED) {
                log.debug("📋 SCENARIO_ADDED details: name={}, actions={}, conditions={}",
                        request.getScenarioAdded().getName(),
                        request.getScenarioAdded().getActionsList().size(),
                        request.getScenarioAdded().getConditionsList().size());
            }

            log.debug("Sending HubEvent to Kafka...");
            kafkaEventSender.send(request);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();

            log.info("✅ [gRPC] HubEvent processed successfully - hubId: {}", hubId);

        } catch (Exception e) {
            log.error("❌ [gRPC] ERROR in collectHubEvent - hubId: {}, payload: {}, error: {}",
                    hubId, payloadCase, e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to process hub event: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        String hubId = request.getHubId();
        String payloadCase = request.getPayloadCase().name();

        log.info("📥 [gRPC] Received SensorEvent - hubId: {}, type: {}", hubId, payloadCase);

        try {
            log.debug("Sending SensorEvent to Kafka...");
            kafkaEventSender.send(request);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();

            log.info("✅ [gRPC] SensorEvent processed successfully - hubId: {}", hubId);

        } catch (Exception e) {
            log.error("❌ [gRPC] ERROR in collectSensorEvent - hubId: {}, type: {}, error: {}",
                    hubId, payloadCase, e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to process sensor event: " + e.getMessage())
                    .asRuntimeException());
        }
    }
}