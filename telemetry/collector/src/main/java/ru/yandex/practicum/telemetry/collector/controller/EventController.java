package ru.yandex.practicum.telemetry.collector.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.kafka.telemetry.event.BaseDeviceEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.BaseEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceEventAvro;
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

            BaseDeviceEventAvro base = BaseDeviceEventAvro.newBuilder()
                    .setId(deviceEvent.getId())
                    .setHubId(deviceEvent.getHubId())
                    .setTimestamp(deviceEvent.getTimestamp())
                    .build();

            DeviceEventAvro.newBuilder()
                    .setBase(base)
                    .set
        }
    }
}
