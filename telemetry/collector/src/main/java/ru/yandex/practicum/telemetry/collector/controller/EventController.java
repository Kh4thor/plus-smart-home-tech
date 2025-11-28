package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaEventSender;
import ru.yandex.practicum.telemetry.collector.model.hubevent.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventController {
    private final KafkaEventSender kafkaEventSender;

    public EventController(KafkaEventSender kafkaEventSender) {
        this.kafkaEventSender = kafkaEventSender;
    }

    @PostMapping("/sensors")
    public boolean collectSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        log.info("json: {}", sensorEvent.toString());
        return kafkaEventSender.send(sensorEvent);
    }

    @PostMapping("/hubs")
    public boolean collectHubEvent(@RequestBody @Valid HubEvent hubEvent) {
        log.info("json: {}", hubEvent.toString());
        return kafkaEventSender.send(hubEvent);
    }
}