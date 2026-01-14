package ru.yandex.practicum.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {

    String userMessage;

    public SpecifiedProductAlreadyInWarehouseException(String userMessage, UUID productId) {
        super("Specified product with id=" + productId + " already exists in Warehouse");
        this.userMessage = userMessage;
    }
}
