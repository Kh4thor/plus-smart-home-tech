package ru.yandex.practicum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ProductDto;
import ru.yandex.practicum.model.utills.ProductMapper;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.service.ProductService;


@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class Controller {

    private final ProductService productService;

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@RequestBody @Valid ProductDto productDto) {
        Product product = ProductMapper.toProduct(productDto);
        Product created = productService.create(product);
        return ProductMapper.toProductDto(created);
    }
}
