package ru.yandex.practicum.exception.shopping.store;


import lombok.Getter;
import ru.yandex.practicum.exception.CustomException;

import java.util.UUID;

@Getter
public class ProductNotFoundException extends CustomException {

    public ProductNotFoundException(String userMessage, UUID id) {
        super("Product id=" + id + " not found", userMessage);
    }
}