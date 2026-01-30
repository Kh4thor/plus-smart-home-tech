package ru.yandex.practicum.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.exception.order.NoOrderFoundException;
import ru.yandex.practicum.model.payment.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    //TODO
    public Payment createPayment(OrderDto orderDto) {
        return null;
    }

    //TODO
    public Double getTotalCost() {
        return null;
    }

    //TODO
    public void refundPayment(@Valid OrderDto orderDto) {
        String userMessage = "Payment refunded";
        throw new NoOrderFoundException(userMessage, orderDto.getOrderId());
    }

    //TODO
    public Double getProductsCostByOrder(@Valid OrderDto orderDto) {
        return null;
    }

    public void failedPayment(@Valid OrderDto orderDto) {

    }
}
