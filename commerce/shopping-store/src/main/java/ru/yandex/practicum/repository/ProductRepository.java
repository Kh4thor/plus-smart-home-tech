package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.ProductDto;

@Service
public interface ProductRepository extends JpaRepository<ProductDto, String> {
}
