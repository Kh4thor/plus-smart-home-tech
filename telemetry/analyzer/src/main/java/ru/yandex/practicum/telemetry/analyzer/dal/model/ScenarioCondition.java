package ru.yandex.practicum.telemetry.analyzer.dal.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "scenario_conditions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioCondition {
    @EmbeddedId
    ScenarioConditionId id;

    @MapsId("scenarioId")
    @JoinColumn(name = "scenario_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Scenario scenario;

    @MapsId("sensorId")
    @JoinColumn(name = "sensor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Sensor sensor;

    @MapsId("conditionId")
    @JoinColumn(name = "condition_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Condition condition;
}