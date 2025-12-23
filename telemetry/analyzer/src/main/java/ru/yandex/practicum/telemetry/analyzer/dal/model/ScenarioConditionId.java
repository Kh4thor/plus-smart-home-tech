package ru.yandex.practicum.telemetry.analyzer.dal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioConditionId implements Serializable {
    @Column(name = "scenario_id")
    Long scenarioId;

    @Column(name = "sensor_id")
    String sensorId;

    @Column(name = "condition_id")
    Long conditionId;
}