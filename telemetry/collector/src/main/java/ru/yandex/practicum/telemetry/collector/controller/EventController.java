package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.scenario.ScenarioEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.sensor.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hubevent.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.hubevent.scenario.ScenarioEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.DeviceEventHandler;
import ru.yandex.practicum.telemetry.collector.service.SensorEventHandler;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventController {
    private final KafkaEventProducer producer;
    private final SensorEventHandler sensorEventHandler;
    private final DeviceEventHandler deviceEventHandler;

    public EventController(KafkaEventProducer producer, SensorEventHandler sensorEventHandler, DeviceEventHandler deviceEventHandler) {
        this.producer = producer;
        this.sensorEventHandler = sensorEventHandler;
        this.deviceEventHandler = deviceEventHandler;
    }

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        log.info("json: {}", sensorEvent.toString());
        SensorEventAvro sensorEventAvro = sensorEventHandler.toAvro(sensorEvent);
        producer.send(sensorEventAvro);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@RequestBody @Valid HubEvent hubEvent) {
        log.info("json: {}", hubEvent.toString());
        List<HubEventType> deviceEventTypes = List.of(HubEventType.DEVICE_ADDED, HubEventType.DEVICE_REMOVED);
        List<HubEventType> scenarioEventTypes = List.of(HubEventType.SCENARIO_ADDED, HubEventType.SCENARIO_REMOVED);
        HubEventType hubEventType = hubEvent.getType();

        if (deviceEventTypes.contains(hubEventType)) {
            DeviceEvent deviceEvent = (DeviceEvent) hubEvent;
            DeviceEventAvro deviceEventAvro = deviceEventHandler.toAvro(deviceEvent);
            producer.send(deviceEventAvro);
        } else if (scenarioEventTypes.contains(hubEventType)) {
            ScenarioEvent scenarioEvent = (ScenarioEvent) hubEvent;
            ScenarioEventAvro scenarioEventAvro = deviceEventHandler.toAvro(scenarioEvent);
            producer.send(deviceEventAvro);
        }


    }
}