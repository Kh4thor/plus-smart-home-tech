package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.model.Pageable;
import ru.yandex.practicum.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product getById(UUID id);

    Product create(Product product);

    Product update(Product toUpdate);

    boolean updateQuantityState(Product toUpdate);

    boolean remove(UUID id);

    List<ProductDto> findByCategoryAndPageable(ProductCategory category, Pageable pageable);
}
