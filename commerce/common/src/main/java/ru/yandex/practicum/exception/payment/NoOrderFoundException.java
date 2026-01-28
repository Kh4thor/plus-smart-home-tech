package ru.yandex.practicum.exception.payment;

import ru.yandex.practicum.exception.CustomException;

import java.util.UUID;

public class NoOrderFoundException extends CustomException {

    public NoOrderFoundException(String userMessage, UUID orderID) {
        super("Order id=" + orderID + " not found", userMessage);
    }
}