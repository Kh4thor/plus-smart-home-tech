package ru.yandex.practicum.telemetry.collector.model.sensor.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;

@Getter
@SuperBuilder
public class ClimateSensorEvent extends SensorEvent {

    private Integer temperatureC;
    private Integer humidity;
    private Integer co2Level;

    @Override
    public SensorEventType getType(){
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
