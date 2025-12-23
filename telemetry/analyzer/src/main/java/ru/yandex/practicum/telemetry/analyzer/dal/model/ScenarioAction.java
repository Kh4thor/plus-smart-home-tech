package ru.yandex.practicum.telemetry.analyzer.dal.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "scenario_actions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioAction {
    @EmbeddedId
    ScenarioActionId id;

    @MapsId("scenarioId")
    @JoinColumn(name = "scenario_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Scenario scenario;

    @MapsId("sensorId")
    @JoinColumn(name = "sensor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Sensor sensor;

    @MapsId("actionId")
    @JoinColumn(name = "action_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Action action;
}
