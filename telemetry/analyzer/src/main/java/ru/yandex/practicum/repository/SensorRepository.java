package ru.yandex.practicum.repository;

import java.util.Collection;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    boolean existsByIdInAndHubId(Collection<String> ids, String hubId);
    Optional<Sensor> findByIdAndHubId(String id, String hubId);
}
