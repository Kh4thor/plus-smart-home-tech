package ru.yandex.practicum.exception.delivery;

import ru.yandex.practicum.exception.CustomException;

import java.util.UUID;

public class NoDeliveryFoundException extends CustomException {

    public NoDeliveryFoundException(String userMessage, UUID deliveryId) {
        super("Delivery id=" + deliveryId + " not found", userMessage);
    }
}
