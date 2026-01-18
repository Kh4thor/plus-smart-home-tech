package ru.yandex.practicum.exception.shopping.cart;


import lombok.Getter;
import ru.yandex.practicum.exception.CustomException;

import java.util.UUID;

@Getter
public class ShoppingCartNotFoundException extends CustomException {

    public ShoppingCartNotFoundException(String userMessage, UUID id) {
        super("Shopping cart by id=" + id + " not found", userMessage);
    }

    public ShoppingCartNotFoundException(String userMessage, String username) {
        super("Shopping cart by username: " + username + " not found", userMessage);
    }

}
