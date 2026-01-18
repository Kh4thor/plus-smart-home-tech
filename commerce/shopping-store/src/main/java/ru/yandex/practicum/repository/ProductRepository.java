package ru.yandex.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.enums.shopping.store.ProductCategory;
import ru.yandex.practicum.enums.shopping.store.ProductState;
import ru.yandex.practicum.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByProductCategoryAndProductState(
            ProductCategory productCategory,
            ProductState productState,
            Pageable pageable);

    Optional<Product> findByProductIdAndProductState(UUID productId, ProductState productState);
}