package ru.yandex.practicum.kafka;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import telemetry.collector.CollectorControllerGrpc;

@Component
public class EventDataProducer {

    @GrpcClient("collector")
    private CollectorControllerGrpc.CollectorControllerBlockingStub collectorStub;

}