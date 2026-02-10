package ru.yandex.practicum.utils.payment;

import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.model.payment.Payment;

public class PaymentMapper {

    public static PaymentDto toPaymentDto(Payment payment) {
        if (payment == null)
            return null;
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(payment.getTotalPayment())
                .deliveryTotal(payment.getDeliveryTotal())
                .feeTotal(payment.getFeeTotal())
                .build();
    }
}
