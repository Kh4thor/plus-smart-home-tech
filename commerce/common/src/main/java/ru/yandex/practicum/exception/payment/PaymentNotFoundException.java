package ru.yandex.practicum.exception.payment;

import ru.yandex.practicum.exception.CustomException;

import java.util.UUID;

public class PaymentNotFoundException extends CustomException {
    public PaymentNotFoundException(String userMessage, UUID paymentId) {
        super("Payment by id=" + paymentId + " not found", userMessage);
    }
}
