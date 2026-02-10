package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.model.payment.Payment;
import ru.yandex.practicum.service.PaymentService;
import ru.yandex.practicum.utils.payment.PaymentMapper;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto createPayment(@RequestBody @Valid OrderDto orderDto) {
        log.debug("GET /api/v1/payment - orderDto: {}", orderDto);
        Payment payment = paymentService.createPayment(orderDto);
        log.info("Payment created: {}", payment);
        PaymentDto paymentDto = PaymentMapper.toPaymentDto(payment);
        log.info("Payment mapped to dto: {}", paymentDto);
        return paymentDto;
    }

    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    public Double getTotalCost(@RequestBody @Valid OrderDto orderDto) {
        log.debug("POST /api/v1/payment/totalCost - orderDto: {}", orderDto);
        Double totalCost = paymentService.getTotalCost(orderDto);
        log.info("Total cost: {}", totalCost);
        return totalCost;
    }

    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void refundPayment(@RequestBody @Valid UUID paymentId) {
        log.debug("POST /api/v1/payment/refund - orderDto: {}", paymentId);
        paymentService.refundPayment(paymentId);
        log.warn("Payment refunded");
    }

    @PostMapping("/productCost")
    @ResponseStatus(HttpStatus.OK)
    public Double getProductsCostByOrder(@RequestBody @Valid OrderDto orderDto) {
        log.debug("POST /api/v1/payment/productCost - order dto: {}", orderDto);
        Double productsCost = paymentService.getProductsCostByOrder(orderDto);
        log.info("Product cost: {}", productsCost);
        return productsCost;
    }

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void failedPayment(@RequestBody @Valid UUID paymentId) {
        log.debug("POST /api/v1/payment/failed - payment id: {}", paymentId);
        paymentService.failedPayment(paymentId);
    }
}
