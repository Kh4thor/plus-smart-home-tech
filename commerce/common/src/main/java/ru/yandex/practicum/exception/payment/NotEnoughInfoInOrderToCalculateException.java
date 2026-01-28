package ru.yandex.practicum.exception.payment;

import ru.yandex.practicum.exception.CustomException;

import java.util.UUID;

public class NotEnoughInfoInOrderToCalculateException extends CustomException {
    public NotEnoughInfoInOrderToCalculateException(String userMessage, UUID orderID) {
        super("Not enough info in order id=" + orderID + " to calculate", userMessage);
    }
}
