package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.exception.order.NoOrderFoundException;
import ru.yandex.practicum.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    //TODO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto createPayment(@RequestBody @Valid OrderDto orderDto) {
        Order paymentOrder = paymentService.createPayment(orderDto);

    }

    //TODO
    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    public Double getTotalCost(@RequestBody @Valid OrderDto orderDto) {
        return null;
    }

    //TODO
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void refundPayment(@RequestBody @Valid OrderDto orderDto) {
        String userMessage = "Payment refunded";
        throw new NoOrderFoundException(userMessage, orderDto.getOrderId());
    }

    //TODO
    @PostMapping("/productCost")
    @ResponseStatus(HttpStatus.OK)
    public Double getProductsCostByOrder(@RequestBody @Valid OrderDto orderDto) {
        return null;
    }

    //TODO
    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void failedPayment(@RequestBody @Valid OrderDto orderDto) {
    }
}
