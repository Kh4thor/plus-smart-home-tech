package ru.yandex.practicum.model.payment;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import ru.yandex.practicum.enums.payment.PaymentState;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID paymentId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "total_payment")
    private Double totalPayment;

    @Column(name = "delivery_total")
    private Double deliveryTotal;

    @Column(name = "fee_total")
    private Double feeTotal;

    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private PaymentState state;
}
