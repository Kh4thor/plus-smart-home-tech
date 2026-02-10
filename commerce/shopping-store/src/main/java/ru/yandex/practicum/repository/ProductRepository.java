package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.enums.shopping.store.ProductCategory;
import ru.yandex.practicum.enums.shopping.store.ProductState;
import ru.yandex.practicum.model.shopping.store.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findByProductCategoryIn(List<ProductCategory> categories, Pageable pageable);

    Optional<Product> findByProductId(UUID productId);

    Optional<Product> findByProductIdAndProductState(UUID productId, ProductState productState);
}