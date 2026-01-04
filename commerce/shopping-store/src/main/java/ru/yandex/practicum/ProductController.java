package ru.yandex.practicum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.model.Pageable;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.service.ProductService;
import ru.yandex.practicum.utills.ProductMapper;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProducts(@RequestParam ProductCategory category, @RequestParam Pageable pageable) {
        return productService.findByCategoryAndPageable(category, pageable);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto create(@RequestBody @Valid ProductDto productDto) {
        Product product = ProductMapper.toProduct(productDto);
        Product created = productService.create(product);
        return ProductMapper.toProductDto(created);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto update(@RequestBody @Valid ProductDto productDto) {
        Product product = ProductMapper.toProduct(productDto);
        Product updated = productService.update(product);
        return ProductMapper.toProductDto(updated);
    }

    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public boolean removeProductFromStore(@RequestBody UUID productId) {
        return productService.remove(productId);
    }

    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public boolean updateQuantityState(@RequestBody @Valid ProductDto productDto) {
        Product product = ProductMapper.toProduct(productDto);
        return productService.updateQuantityState(product);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProduct(@PathVariable UUID productId) {
        Product product = productService.getById(productId);
        return ProductMapper.toProductDto(product);
    }

    private Pageable toPageable(String json) throws JsonProcessingException {
        try {
            return objectMapper.readValue(json, Pageable.class);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            throw new BadRequestException("Expected fields: page, size, sort");
        }
    }
}
