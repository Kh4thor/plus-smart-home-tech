package ru.yandex.practicum.feign.shopping.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
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
    ShoppingCartDto putShoppingCart(
            @RequestBody @Valid ShoppingCartDto shoppingCartDto,
            @RequestParam @NotBlank String username);

    @DeleteMapping
    boolean deleteShoppingCart(@RequestParam @NotBlank String username);

    @PostMapping("/remove")
    ShoppingCartDto removeShoppingCart(
            @RequestParam @NotBlank String username,
            @RequestBody List<UUID> productIds);

    @PostMapping("change-quantity")
    ShoppingCartDto changeQuantity(
            @RequestParam @NotBlank String username,
            @RequestBody ChangeQuantityDto changeQuantityDto);
}
