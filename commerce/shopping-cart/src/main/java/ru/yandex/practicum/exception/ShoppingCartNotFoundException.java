package ru.yandex.practicum.exception;


import javassist.NotFoundException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ShoppingCartNotFoundException extends NotFoundException {
    private final String errorMessage;

    public ShoppingCartNotFoundException(String errorMessage, UUID id) {
        super("Product id=" + id + " not found");
        this.errorMessage = errorMessage;
    }

}
