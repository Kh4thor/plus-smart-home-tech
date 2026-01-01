package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ProductDto;

import java.util.UUID;

@Service
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
