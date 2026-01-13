package ru.yandex.practicum.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class WarehouseProductNotFoundException extends RuntimeException {
    private final String userMessage;

    public WarehouseProductNotFoundException(String userMessage, UUID productId) {
        super("Warehouse product with id " + productId + " not found");
        this.userMessage = userMessage;
    }
}
