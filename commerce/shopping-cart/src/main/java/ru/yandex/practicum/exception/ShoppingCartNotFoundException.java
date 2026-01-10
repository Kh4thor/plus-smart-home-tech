package ru.yandex.practicum.exception;


import lombok.Getter;

import java.util.UUID;

@Getter
public class ShoppingCartNotFoundException extends RuntimeException {
    private final String userMessage;

    public ShoppingCartNotFoundException(String userMessage, UUID id) {
        super("Shopping cart id=" + id + " not found");
        this.userMessage = userMessage;
    }

    public ShoppingCartNotFoundException(String userMessage, String username) {
        super("Shopping cart by username: " + username + " not found");
        this.userMessage = userMessage;
    }

}
