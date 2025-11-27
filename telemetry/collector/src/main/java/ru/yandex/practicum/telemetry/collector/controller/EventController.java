package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.scenario.ScenarioEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.sensor.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaEventService;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.DeviceEventHandler;
import ru.yandex.practicum.telemetry.collector.service.ScenarioEventHandler;
import ru.yandex.practicum.telemetry.collector.service.SensorEventHandler;
import ru.yandex.practicum.telemetry.collector.service.TopicHandler;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/events/")
public class EventController {

    private final TopicHandler topicHandler;
    private final KafkaEventService kafkaEventService;
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<Class<? extends HubEvent>, DeviceEventHandler> deviceEventHandlers;
    private final Map<Class<? extends HubEvent>, ScenarioEventHandler> scenarioEventHandlers;

    // Конструктор с параметрами
    public EventController(
            TopicHandler topicHandler,
            KafkaEventService kafkaEventService,
            Map<SensorEventType, SensorEventHandler> sensorEventHandlers,
            Map<Class<? extends HubEvent>, DeviceEventHandler> deviceEventHandlers,
            Map<Class<? extends HubEvent>, ScenarioEventHandler> scenarioEventHandlers) {
        this.topicHandler = topicHandler;
        this.kafkaEventService = kafkaEventService;
        this.sensorEventHandlers = sensorEventHandlers;
        this.deviceEventHandlers = deviceEventHandlers;
        this.scenarioEventHandlers = scenarioEventHandlers;
    }

    @PostMapping("/sensors")
    public void sensorEvent(@RequestBody @Valid SensorEvent sensorEvent) {
        log.info("getting sensor event {}", sensorEvent);
        String topic = topicHandler.getTopic(sensorEvent);
        // Получаем обработчик по типу сенсорного события
        SensorEventHandler sensorEventHandler = sensorEventHandlers.get(sensorEvent.getType());
        if (sensorEventHandler == null) {
            log.error("No handler found for sensor event type: {}", sensorEvent.getType());
            return;
        }
        SensorEventAvro sensorEventAvro = sensorEventHandler.toAvro(sensorEvent);
        kafkaEventService.send(topic, sensorEventAvro);
    }

    @PostMapping("/hubs")
    public void hubEvent(@RequestBody @Valid HubEvent hubEvent) {
        log.info("getting hub event {}", hubEvent);
        String topic = topicHandler.getTopic(hubEvent);

        if (hubEvent instanceof DeviceEvent) {
            DeviceEventHandler deviceEventHandler = deviceEventHandlers.get(hubEvent.getClass());
            DeviceEventAvro deviceEventAvro = deviceEventHandler.toAvro((DeviceEvent) hubEvent);
            kafkaEventService.send(topic, deviceEventAvro);

        } else if (hubEvent instanceof ScenarioEvent) {
            ScenarioEventHandler scenarioEventHandler = scenarioEventHandlers.get(hubEvent.getClass());
            ScenarioEventAvro scenarioEventAvro = scenarioEventHandler.toAvro((ScenarioEvent) hubEvent);
            kafkaEventService.send(topic, scenarioEventAvro);
        } else {
            log.error("Unsupported hub event type: {}", hubEvent.getClass());
        }
    }
}