package ru.yandex.practicum.telemetry.collector.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEventType;
import ru.yandex.practicum.telemetry.collector.model.device.events.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;

import java.util.Map;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final Map<SensorEventType, SensorEvent> sensorEventMap;
    private final Map<DeviceEventType, DeviceEvent> deviceEventMap;

    @PostMapping()
    public void post(DeviceEvent deviceEvent) {
        if (deviceEvent.getType() == DeviceEventType.DEVICE_ADDED) {
            DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) deviceEvent;
//            HubEventAvro.newBuilder...
//            producer.send(avro);
        }
    }
}
