package ru.yandex.practicum.feign.warehouse;


import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface FeignClientWarehouse {

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    void registerNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request) throws FeignException;

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    BookedProductsDto checkProductQuantity(@RequestBody @Valid ShoppingCartDto shoppingCartDto) throws FeignException;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request) throws FeignException;

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    AddressDto getAddress();
}
