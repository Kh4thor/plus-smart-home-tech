package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.service.WarehouseService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void registerNewProduct(@RequestBody @Valid NewProductInWarehouseRequest newProduct) {
        warehouseService.registerNewProduct(newProduct);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto checkProductQuantity(@RequestBody @Valid ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkProductQuantity(shoppingCartDto);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request) {
        warehouseService.addProduct(request);
    }

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getAddress(@RequestBody @Valid AddressDto addressDto) {
        return warehouseService.getAddress(addressDto);
    }
}
