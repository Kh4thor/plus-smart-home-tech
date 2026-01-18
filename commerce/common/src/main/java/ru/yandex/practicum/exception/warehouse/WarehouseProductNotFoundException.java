package ru.yandex.practicum.exception.warehouse;

import lombok.Getter;
import ru.yandex.practicum.exception.CustomException;

import java.util.UUID;

@Getter
public class WarehouseProductNotFoundException extends CustomException {

    public WarehouseProductNotFoundException(String userMessage, UUID productId) {
        super("Warehouse product with id " + productId + " not found", userMessage);
    }
}
