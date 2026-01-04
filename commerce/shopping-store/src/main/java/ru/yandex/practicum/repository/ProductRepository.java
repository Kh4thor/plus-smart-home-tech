package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.PageableDto;
import ru.yandex.practicum.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public interface ProductRepository extends JpaRepository<Product, UUID>, QuerydslPredicateExecutor<Product> {

    public default List<Product> findByFilter(PageableDto pageableDto) {
        return new ArrayList<>();
    }
}