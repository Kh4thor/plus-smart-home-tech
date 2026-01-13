package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.WarehouseProduct;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<WarehouseProduct, UUID> {

    Optional<WarehouseProduct> findByProductId(UUID productId);

    Map<UUID, WarehouseProduct> findAllAsMapByIds(Set<UUID> productIds);
}