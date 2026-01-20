package ru.yandex.practicum.feign.shopping.cart;

import feign.FeignException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shopping.cart.ChangeQuantityDto;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;

import java.util.List;
import java.util.UUID;

@Validated
@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface FeignClientShoppingCart {

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto putShoppingCart(
            @RequestBody @Valid ShoppingCartDto shoppingCartDto,
            @RequestParam @NotBlank String username) throws FeignException;

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    boolean deleteShoppingCart(@RequestParam @NotBlank String username) throws FeignException;

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto removeShoppingCart(
            @RequestParam @NotBlank String username,
            @RequestBody List<UUID> productIds) throws FeignException;

    @PostMapping("change-quantity")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto changeQuantity(
            @RequestParam @NotBlank String username,
            @RequestBody ChangeQuantityDto changeQuantityDto) throws FeignException;
}
