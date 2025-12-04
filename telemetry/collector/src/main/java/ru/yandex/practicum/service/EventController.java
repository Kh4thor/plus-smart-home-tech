package ru.yandex.practicum.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import telemetry.collector.CollectorControllerGrpc;

@GrpcService
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
    }
}
