package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.WarehouseProduct;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseProduct, UUID> {

    List<WarehouseProduct> findByProductIdIn(Set<UUID> productIds);

    Optional<WarehouseProduct> findByProductId(UUID productId);

    boolean existsByProductId(UUID productId);
}