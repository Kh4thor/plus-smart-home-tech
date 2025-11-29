package ru.yandex.practicum.telemetry.collector.model.sensor.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MotionSensorEvent extends SensorEvent {

    private Boolean motionDetected;
    private Integer sensitivity;

   @Override
    public SensorEventType getType(){
       return SensorEventType.MOTION_SENSOR_EVENT;
   }
}