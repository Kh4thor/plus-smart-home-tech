package ru.yandex.practicum.telemetry.analyzer.dal.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.dal.model.Action;
import ru.yandex.practicum.telemetry.analyzer.dal.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.dal.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.model.enums.ConditionOperation;
import ru.yandex.practicum.telemetry.analyzer.dal.model.enums.ConditionType;
import ru.yandex.practicum.telemetry.analyzer.dal.model.enums.DeviceActionType;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.SensorRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;
    private final SensorRepository sensorRepository;

    public Scenario save(ScenarioAddedEventAvro scenarioAddedEventAvro, String hubId) {
        Set<String> sensors = new HashSet<>();
        scenarioAddedEventAvro.getConditions().forEach(condition -> sensors.add(condition.getSensorId()));

        boolean allSensorsExists = sensorRepository.existsByIdInAndHubId(sensors, hubId);
        if (!allSensorsExists) {
            throw new IllegalStateException("Нет возможности создать сценарий с использованием неизвестного устройства");
        }

        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(hubId, scenarioAddedEventAvro.getName());

        Scenario scenario;
        if (scenarioOpt.isEmpty()) {
            scenario = new Scenario();
            scenario.setName(scenarioAddedEventAvro.getName());
            scenario.setHubId(hubId);
        } else {
            scenario = scenarioOpt.get();

            Map<String, Condition> conditions = scenario.getConditions();
            conditionRepository.deleteAll(conditions.values());
            scenario.getConditions().clear();

            Map<String, Action> actions = scenario.getActions();
            actionRepository.deleteAll(actions.values());
            scenario.getActions().clear();
        }

        for (ScenarioConditionAvro scenarioConditionAvro : scenarioAddedEventAvro.getConditions()) {
            Condition condition = new Condition();

            ConditionTypeAvro conditionTypeAvro = scenarioConditionAvro.getType();
            ConditionType conditionType = ConditionType.from(conditionTypeAvro);
            condition.setType(conditionType);

            ConditionOperationAvro conditionOperationAvro = scenarioConditionAvro.getOperation();
            ConditionOperation conditionOperation = ConditionOperation.from(conditionOperationAvro);
            condition.setOperation(conditionOperation);
            condition.setValue(mapValue(scenarioConditionAvro.getValue()));

            scenario.addCondition(scenarioConditionAvro.getSensorId(), condition);
        }

        for (DeviceActionAvro deviceActionAvro : scenarioAddedEventAvro.getActions()) {
            Action action = new Action();

            ActionTypeAvro actionTypeAvro = deviceActionAvro.getType();
            DeviceActionType deviceActionType = DeviceActionType.from(actionTypeAvro);
            action.setType(deviceActionType);
            if (deviceActionAvro.getType().equals(ActionTypeAvro.SET_VALUE)) {
                action.setValue(mapValue(deviceActionAvro.getValue()));
            }

            scenario.addAction(deviceActionAvro.getSensorId(), action);
        }
        conditionRepository.saveAll(scenario.getConditions().values());
        actionRepository.saveAll(scenario.getActions().values());
        return scenarioRepository.save(scenario);
    }

    public void delete(String name, String hubId) {
        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(hubId, name);

        String message = String.format("Scenario with name '%s' and hubId '%s' not found", name, hubId);
        Scenario scenario = scenarioOpt.orElseThrow(() -> new EntityNotFoundException(message));

        conditionRepository.deleteAll(scenario.getConditions().values());
        actionRepository.deleteAll(scenario.getActions().values());
        scenarioRepository.delete(scenario);
    }

    private Integer mapValue(Object value) {
        if (value != null) {
            if (value instanceof Integer i) return i;
            if (value instanceof Boolean b) return b ? 1 : 0;
        }
        return null;
    }
}