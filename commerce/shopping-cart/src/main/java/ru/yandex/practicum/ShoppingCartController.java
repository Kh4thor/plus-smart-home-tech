package ru.yandex.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.service.ShoppingCartMapper;
import ru.yandex.practicum.service.ShoppingCartService;

@RestController
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {

    ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        String validatedUsername = validateUsername(username);
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(validatedUsername);
        return ShoppingCartMapper.toShoppingCartDto(shoppingCart);
    }








    private String validateUsername(String username) {
        if (username == null || username.isBlank()) {
            String errorMessage = "Username cannot be empty";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be empty");
        }
        return username.trim();
    }
}
