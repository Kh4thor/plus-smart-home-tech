package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.kafka.telemetry.event.sensor.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.EventKafkaProducer;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handler.hub.ScenarioEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handler.sensor.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events/")
public class EventController {

    private final EventKafkaProducer producer;

    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEvent, HubEventHandler> hubEventHandlers;


    public EventController(List<ScenarioEventHandler> scenarioEventHandlers, List<HubEventHandler> hubEventHandlers){
        this.sensorEventHandlers = scenarioEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::get))
    }

    this.sensorEventHandlers

    @PostMapping("/sensors")
    public void sensorEvent(@RequestBody @Valid SensorEvent sensorEvent) {
        log.info("getting sensor event {}", sensorEvent);
        SensorEventAvro sensorEventAvro = sensorEventHandler.toAvro(sensorEvent);
        producer.send(sensorEvent);
    }

    @PostMapping("/hubs")
    public void hubEvent(@RequestBody @Valid DeviceEvent deviceEvent) {
        log.info("getting hub event {}", deviceEvent);
        service.sendHubEvent(deviceEvent);
    }
}