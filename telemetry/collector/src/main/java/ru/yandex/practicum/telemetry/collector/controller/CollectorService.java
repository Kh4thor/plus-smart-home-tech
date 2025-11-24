package ru.yandex.practicum.telemetry.collector.controller;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.device.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.scenario.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.scenario.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.scenario.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.scenario.ScenarioRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.sensor.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.model.device.DeviceEvent;
import ru.yandex.practicum.telemetry.collector.model.device.events.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.device.events.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.model.scenario.events.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.scenario.events.ScenarioRemovedEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.events.*;

@Service
@RequiredArgsConstructor
public class CollectorService {

    @Value("${topic.sensor-event}")
    private final String sensorTopic = "telemetry.sensors.v1";
    @Value("${topic.hub-event}")
    private final String hubTopic = "telemetry.hubs.v1";

    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    public void sendSensorEvent(SensorEvent sensorEvent) {
        kafkaTemplate.send(sensorTopic, sensorEvent.getHubId(), mapToRecord(sensorEvent));
    }

    public void sendHubEvent(DeviceEvent deviceEvent) {
        kafkaTemplate.send(hubTopic, deviceEvent.getHubId(), mapToRecord(deviceEvent));
    }

    private SpecificRecordBase mapToRecord(HubEvent hubEvent) {
        HubEventType type = hubEvent.getType();
        return switch (type) {
            case DEVICE_ADDED -> {
                DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) hubEvent;
                yield new HubEventAvro(
                        deviceAddedEvent.getHubId(),
                        deviceAddedEvent.getTimestamp(),
                        new DeviceAddedEventAvro(deviceAddedEvent.getId(),
                                DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name())));
            }
            case DEVICE_REMOVED -> {
                DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) hubEvent;
                yield new HubEventAvro(
                        deviceRemovedEvent.getHubId(),
                        deviceRemovedEvent.getTimestamp(),
                        new DeviceRemovedEventAvro(deviceRemovedEvent.getId())
                );
            }
            case SCENARIO_ADDED -> {
                ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) hubEvent;
                yield new HubEventAvro(
                        scenarioAddedEvent.getHubId(),
                        scenarioAddedEvent.getTimestamp(),
                        new ScenarioAddedEventAvro(
                                scenarioAddedEvent.getName(),
                                scenarioAddedEvent.getConditions().stream()
                                        .map(sc -> new ScenarioConditionAvro(
                                                sc.getSensorId(),
                                                ConditionTypeAvro.valueOf(sc.getType().name()),
                                                ConditionOperationAvro.valueOf(sc.getOperation().name()),
                                                sc.getValue()))
                                        .toList(),
                                scenarioAddedEvent.getActions().stream()
                                        .map(da -> new DeviceActionAvro(
                                                da.getSensorId(),
                                                ActionTypeAvro.valueOf(da.getType().name()),
                                                da.getValue()))
                                        .toList()
                        ));
            }
            case SCENARIO_REMOVED -> {
                ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) hubEvent;
                yield new HubEventAvro(
                        scenarioRemovedEvent.getHubId(),
                        scenarioRemovedEvent.getTimestamp(),
                        new ScenarioRemovedEventAvro(scenarioRemovedEvent.getName())
                );
            }
        };
    }

    private SpecificRecordBase mapToRecord(SensorEvent sensorEvent) {
        SensorEventType type = sensorEvent.getType();
        return switch (type) {
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) sensorEvent;
                yield new SensorEventAvro(
                        temperatureSensorEvent.getId(),
                        temperatureSensorEvent.getHubId(),
                        temperatureSensorEvent.getTimestamp(),
                        new TemperatureSensorAvro(
                                temperatureSensorEvent.getTemperatureC(),
                                temperatureSensorEvent.getTemperatureF()));
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorEvent motionSensorEvent = (MotionSensorEvent) sensorEvent;
                yield new SensorEventAvro(
                        motionSensorEvent.getId(),
                        motionSensorEvent.getHubId(),
                        motionSensorEvent.getTimestamp(),
                        new MotionSensorAvro(
                                motionSensorEvent.getLinkQuality(),
                                motionSensorEvent.getMotion(),
                                motionSensorEvent.getVoltage()));
            }
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) sensorEvent;
                yield new SensorEventAvro(
                        climateSensorEvent.getId(),
                        climateSensorEvent.getHubId(),
                        climateSensorEvent.getTimestamp(),
                        new ClimateSensorAvro(
                                climateSensorEvent.getTemperatureC(),
                                climateSensorEvent.getHumidity(),
                                climateSensorEvent.getCo2Level()));
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorEvent lightSensorEvent = (LightSensorEvent) sensorEvent;
                yield new SensorEventAvro(
                        lightSensorEvent.getId(),
                        lightSensorEvent.getHubId(),
                        lightSensorEvent.getTimestamp(),
                        new LightSensorAvro(
                                lightSensorEvent.getLinkQuality(),
                                lightSensorEvent.getLuminosity()));
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) sensorEvent;
                yield new SensorEventAvro(
                        switchSensorEvent.getId(),
                        switchSensorEvent.getHubId(),
                        switchSensorEvent.getTimestamp(),
                        new SwitchSensorAvro(switchSensorEvent.getState()));
            }
        };
    }

}