package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.WarehouseProduct;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public interface WarehouseRepository extends JpaRepository<WarehouseProduct, UUID> {

    default Map<UUID, WarehouseProduct> findAllAsMapByIds(Set<UUID> productIds) {
        return findAllById(productIds).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, product -> product));
    }

    Optional<WarehouseProduct> findByProductId(UUID productId);

    boolean existsByProductId(UUID productId);
}