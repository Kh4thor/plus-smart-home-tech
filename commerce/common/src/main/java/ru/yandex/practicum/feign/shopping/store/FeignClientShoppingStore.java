package ru.yandex.practicum.feign.shopping.store;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shopping.store.ProductDto;
import ru.yandex.practicum.dto.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.shopping.store.ProductCategory;

import java.util.List;
import java.util.UUID;

@Validated
@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface FeignClientShoppingStore {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ProductDto> getAllProducts(
            @RequestParam ProductCategory category,
            @RequestParam Pageable pageable) throws FeignException;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    ProductDto create(@RequestBody @Valid ProductDto productDto) throws FeignException;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    ProductDto update(@RequestBody @Valid ProductDto productDto) throws FeignException;

    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    boolean removeProductFromStore(@RequestBody UUID productId) throws FeignException;

    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    boolean updateQuantityState(@RequestBody @Valid SetProductQuantityStateRequest request) throws FeignException;

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    ProductDto getProduct(@PathVariable UUID productId) throws FeignException;
}
