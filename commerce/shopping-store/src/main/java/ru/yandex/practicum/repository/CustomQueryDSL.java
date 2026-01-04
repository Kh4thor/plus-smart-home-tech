package ru.yandex.practicum.repository;

import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.model.Pageable;
import ru.yandex.practicum.model.Product;

import java.util.List;

public interface CustomQueryDSL {
    public List<Product> findByCategoryAndPageable(ProductCategory category, Pageable pageable);
}
