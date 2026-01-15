package ru.yandex.practicum.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductNotFoundException extends RuntimeException {

    String userMessage;

    public ProductNotFoundException(String userMessage, UUID productId) {
        super("Unable to find product with id=" + productId);
        this.userMessage = userMessage;
    }
}
