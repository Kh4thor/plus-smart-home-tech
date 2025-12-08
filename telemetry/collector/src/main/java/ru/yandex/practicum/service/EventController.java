package ru.yandex.practicum.service;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import telemetry.collector.CollectorControllerGrpc;

@GrpcService
public class EventController
        extends CollectorControllerGrpc.CollectorControllerImplBase {


    /**
     * Метод для обработки событий от датчиков.
     * Вызывается при получении нового события от gRPC-клиента.
     *
     * @param request          Событие от датчика
     * @param responseObserver Ответ для клиента
     */
    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            // проверяем, есть ли обработчик для полученного события
            // если обработчик найден, передаём событие ему на обработку

//                SensorEventProto sensorEventProto = sensorEventHandlers.get(request.getPayloadCase()).toProto(request);





            // после обработки события возвращаем ответ клиенту
            responseObserver.onNext(Empty.getDefaultInstance());
            // и завершаем обработку запроса
            responseObserver.onCompleted();
        } catch (Exception e) {
            // в случае исключения отправляем ошибку клиенту
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }
}