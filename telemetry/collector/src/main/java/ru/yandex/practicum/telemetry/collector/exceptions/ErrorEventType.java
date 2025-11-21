package ru.yandex.practicum.telemetry.collector.exceptions;

import lombok.Getter;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.scenario.ScenarioEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ErrorEventType extends RuntimeException {

    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String message = "Не найден обработчик для ";

    private final String timestamp;

    public ErrorEventType(DeviceEvent event) {
        super(message + event.getType());
        this.timestamp = getCurrentTime();
    }

    public ErrorEventType(SensorEvent event) {
        super(message + event.getType());
        this.timestamp = getCurrentTime();
    }

    public ErrorEventType(ScenarioEvent event) {
        super(message + event.getType());
        this.timestamp = getCurrentTime();
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(DT_FORMATTER);
    }


}