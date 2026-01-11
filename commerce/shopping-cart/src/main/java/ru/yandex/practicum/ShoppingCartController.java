package ru.yandex.practicum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.service.ShoppingCartMapper;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {

    ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam @NotBlank String username) {
        ShoppingCart retrieved = shoppingCartService.getShoppingCart(username);
        return ShoppingCartMapper.toShoppingCartDto(retrieved);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto putShoppingCart(
            @RequestBody @Valid ShoppingCartDto shoppingCartDto,
            @RequestParam @NotBlank String username) {
        ShoppingCart saved = shoppingCartService.save(shoppingCartDto, username);
        return ShoppingCartMapper.toShoppingCartDto(saved);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteShoppingCart(@RequestParam @NotBlank String username) {
        return shoppingCartService.deleteShoppingCartBy(username);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeShoppingCart(
            @RequestParam @NotBlank String username,
            @RequestBody List<UUID> productIds) {
        ShoppingCart shoppingCart = shoppingCartService.removeShoppingCartBy(username.trim(), productIds);
        return ShoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @PostMapping("change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public void changeQuantity()
}
