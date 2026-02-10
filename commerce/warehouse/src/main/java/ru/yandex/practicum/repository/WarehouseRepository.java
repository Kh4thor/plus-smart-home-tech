package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.warehouse.WarehouseProduct;

import java.util.*;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseProduct, UUID> {

    List<WarehouseProduct> findByProductIdIn(Set<UUID> productIds);

    Optional<WarehouseProduct> findByProductId(UUID productId);

    boolean existsByProductId(UUID productId);

    List<WarehouseProduct> findAllByProductIdIn(List<UUID> productIds);
}