package ru.yandex.practicum.exception.warehouse;

import lombok.Getter;
import ru.yandex.practicum.exception.CustomException;

import java.util.UUID;

@Getter
public class SpecifiedProductAlreadyInWarehouseException extends CustomException {

    public SpecifiedProductAlreadyInWarehouseException(String userMessage, UUID productId) {
        super("Specified product with id=" + productId + " already exists in warehouse", userMessage);
    }
}
