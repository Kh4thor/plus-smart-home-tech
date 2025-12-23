package ru.yandex.practicum.telemetry.analyzer.dal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.telemetry.analyzer.dal.model.enums.ConditionOperation;
import ru.yandex.practicum.telemetry.analyzer.dal.model.enums.ConditionType;

@Entity
@Getter
@Setter
@Table(name = "conditions")
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConditionType type;

    @Enumerated(EnumType.STRING)
    private ConditionOperation operation;

    private Integer value;

    @Transient
    public boolean check(int sensorValue) {
        return operation.apply(sensorValue, this.value);
    }
}
