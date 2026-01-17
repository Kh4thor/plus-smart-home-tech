package ru.yandex.practicum.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product getById(UUID id);

    Product create(Product product);

    Product update(Product toUpdate);

    boolean updateQuantityState(SetProductQuantityStateRequest request);

    boolean remove(UUID id);

    List<Product> findByCategory(ProductCategory category, Pageable pageable);
}
