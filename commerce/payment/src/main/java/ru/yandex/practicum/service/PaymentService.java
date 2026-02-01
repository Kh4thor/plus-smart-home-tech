package ru.yandex.practicum.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.shopping.store.ProductDto;
import ru.yandex.practicum.enums.payment.PaymentState;
import ru.yandex.practicum.exception.payment.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.exception.payment.PaymentNotFoundException;
import ru.yandex.practicum.feign.order.FeignClientOrder;
import ru.yandex.practicum.feign.shopping.store.FeignClientShoppingStore;
import ru.yandex.practicum.model.payment.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final FeignClientOrder feignClientOrder;
    private final FeignClientShoppingStore feignClientShoppingStore;
    private static final double FEE_TAX = 0.15;

    @Transactional
    public Payment createPayment(OrderDto orderDto) {
        String userMessage = "Unable to create payment";
        Payment payment = Payment.builder()
                .orderId(orderDto.getOrderId())
                .totalPayment(getTotalCost(orderDto, userMessage))
                .deliveryTotal(getDeliveryPrice(orderDto, userMessage))
                .feeTotal(getFeeTotal(orderDto, userMessage))
                .state(PaymentState.PENDING)
                .build();
        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Double getTotalCost(OrderDto orderDto) {
        String userMessage = "Unable to calculate total cost";
        return getTotalCost(orderDto, userMessage);
    }

    public void refundPayment(@Valid UUID paymentId) {
        String userMessage = "Unable to refund payment";
        Payment payment = getPaymentById(paymentId, userMessage);
        payment.setState(PaymentState.SUCCESS);
        OrderDto orderDto = feignClientOrder.makePaymentByOrderId(paymentId);
    }

    public Double getProductsCostByOrder(OrderDto orderDto) {
        Map<UUID, Integer> products = orderDto.getProducts();
        double productsCost = 0.0;

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();
            ProductDto productDto = feignClientShoppingStore.getProduct(productId);
            Double productPricePerUnit = productDto.getPrice();
            productsCost += productPricePerUnit * quantity;
        }
        return productsCost;
    }

    @Transactional
    public void failedPayment(UUID paymentId) {
        String userMessage = "Payment failed";
        Payment payment = getPaymentById(paymentId, userMessage);
        payment.setState(PaymentState.FAILED);
        OrderDto orderDto = feignClientOrder.makePaymentByOrderId(paymentId);
    }

    private Payment getPaymentById(UUID paymentId, String userMessage) {
        return paymentRepository.findByPaymentId(paymentId).orElseThrow(() ->
                new PaymentNotFoundException(userMessage, paymentId)
        );
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
