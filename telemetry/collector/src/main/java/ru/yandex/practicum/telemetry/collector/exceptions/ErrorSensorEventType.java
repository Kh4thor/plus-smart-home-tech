package ru.yandex.practicum.telemetry.collector.exceptions;

import lombok.Getter;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ErrorSensorEventType extends RuntimeException {

    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String timestamp;

    public ErrorSensorEventType(SensorEvent sensorEvent) {
        super("Не найден обработчик для " + sensorEvent.getType());
        this.timestamp = LocalDateTime.now().format(DT_FORMATTER);
    }
}