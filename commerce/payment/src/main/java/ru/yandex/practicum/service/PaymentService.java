package ru.yandex.practicum.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.enums.payment.PaymentState;
import ru.yandex.practicum.exception.order.NoOrderFoundException;
import ru.yandex.practicum.exception.payment.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.feign.order.FeignClientOrder;
import ru.yandex.practicum.feign.payment.FeignClientPayment;
import ru.yandex.practicum.model.payment.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final FeignClientOrder feignClientOrder;
    private final FeignClientPayment feignClientPayment;
    private static final double FEE_TAX = 0.15;

    //TODO
    @Transactional
    public Payment createPayment(OrderDto orderDto) {
        String userMessage = "Unable to create payment";

        Payment payment = Payment.builder().orderId(orderDto.getOrderId()).totalPayment(getTotalCost(orderDto, userMessage)).deliveryTotal(getDeliveryPrice(orderDto, userMessage)).feeTotal(getFeeTotal(orderDto, userMessage)).state(PaymentState.PENDING).build();

        return paymentRepository.save(payment);
    }

    //TODO
    public Double getTotalCost(OrderDto orderDto) {
        String userMessage = "Unable to calculate total cost";
        return getTotalCost(orderDto, userMessage);
    }

    @Transactional
    public void successfulPayment(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setState(PaymentState.SUCCESS);
        OrderDto orderDto = feignClientOrder.makePaymentByOrderId(paymentId);
    }

    //TODO
    public void refundPayment(@Valid OrderDto orderDto) {
        UUID paymentId = orderDto.getPaymentId();
        Payment payment = getPayment(UUID paymentId);
        payment.setState(PaymentState.PENDING);
        String userMessage = "Payment refunded";
        throw new NoOrderFoundException(userMessage, orderDto.getOrderId());
    }

    private Payment getPayment(UUID paymentId) {
        feignClientPayment.ge
    }

    //TODO
    public Double getProductsCostByOrder(OrderDto orderDto) {
        return null;
    }

    public void failedPayment(OrderDto orderDto) {

    }

    private double getProductPrice(OrderDto orderDto, String userMessage) {
        if (orderDto.getProductPrice() < 0)
            throw new NotEnoughInfoInOrderToCalculateException(userMessage, orderDto.getOrderId());
        return orderDto.getProductPrice();
    }

    private double getDeliveryPrice(OrderDto orderDto, String userMessage) {
        if (orderDto.getDeliveryPrice() < 0)
            throw new NotEnoughInfoInOrderToCalculateException(userMessage, orderDto.getOrderId());
        return orderDto.getDeliveryPrice();
    }

    private double getFeeTotal(OrderDto orderDto, String userMessage) {
        double productPrice = getProductPrice(orderDto, userMessage);
        if (productPrice < 0) throw new NotEnoughInfoInOrderToCalculateException(userMessage, orderDto.getOrderId());
        return productPrice * FEE_TAX;
    }

    private Double getTotalCost(OrderDto orderDto, String userMessage) {
        double productPrice = orderDto.getProductPrice();
        double deliveryPrice = getDeliveryPrice(orderDto, userMessage);
        double feePrice = getFeeTotal(orderDto, userMessage);

        return productPrice + deliveryPrice + feePrice;
    }
}
