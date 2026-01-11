package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void registerProduct(@RequestBody @Valid NewProductInWarehouseRequest newProduct) {
        warehouseService.registerProduct(newProduct);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public void checkProductQuantity(@RequestBody @Valid ShoppingCartDto shoppingCartDto) {
        warehouseService.checkProductQuantity(shoppingCartDto);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request) {
        warehouseService.addProduct(request);
    }

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public void getAddress(@RequestBody @Valid AddressDto addressDto) {
        warehouseService.getAddress(addressDto);
    }
}
