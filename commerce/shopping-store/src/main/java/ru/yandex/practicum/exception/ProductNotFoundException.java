package ru.yandex.practicum.exception;


import jakarta.ws.rs.NotFoundException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductNotFoundException extends NotFoundException {
    private final String userMessage;

    public ProductNotFoundException(String errorMessage, UUID id) {
        super("Product id=" + id + " not found");
        this.userMessage = errorMessage;
    }
}