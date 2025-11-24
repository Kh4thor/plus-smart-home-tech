package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

@Slf4j
@RestController
@RequestMapping("/events/")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorService service;

    @PostMapping("/sensors")
    public void sensorEvent(@RequestBody @Valid SensorEvent sensorEvent) {
        log.info("getting sensor event {}", sensorEvent);
        service.sendSensorEvent(sensorEvent);
    }

    @PostMapping("/hubs")
    public void hubEvent(@RequestBody @Valid DeviceEvent deviceEvent) {
        log.info("getting hub event {}", deviceEvent);
        service.sendHubEvent(deviceEvent);
    }
}