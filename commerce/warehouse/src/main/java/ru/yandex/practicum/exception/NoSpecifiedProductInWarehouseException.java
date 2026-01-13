package ru.yandex.practicum.exception;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class NoSpecifiedProductInWarehouseException extends RuntimeException {

    private final String userMessage;

    public NoSpecifiedProductInWarehouseException(String userMessage, List<UUID> notFoundProducts) {
        super("No specified products in warehouse: " + notFoundProducts);
        this.userMessage = userMessage;
    }
}
