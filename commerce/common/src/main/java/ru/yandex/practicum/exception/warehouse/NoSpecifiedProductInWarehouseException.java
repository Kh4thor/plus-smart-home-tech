package ru.yandex.practicum.exception.warehouse;

import lombok.Getter;
import ru.yandex.practicum.exception.CustomException;

import java.util.List;
import java.util.UUID;

@Getter
public class NoSpecifiedProductInWarehouseException extends CustomException {

    public NoSpecifiedProductInWarehouseException(String userMessage, List<UUID> notFoundProducts) {
        super("No specified products in warehouse: " + notFoundProducts, userMessage);
    }
}
