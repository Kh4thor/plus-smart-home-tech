package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import ru.yandex.practicum.enums.delivery.DeliveryState;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    private UUID deliveryId;

    @ManyToOne
    @JoinColumn(name = "from_address_id")
    private Address fromAddress;

    @ManyToOne
    @JoinColumn(name = "to_address_id")
    private Address toAddress;

    @Column(name = "order_id")
    private UUID orderID;

    @Column(name = "delivery_state")
    @Enumerated(value = EnumType.STRING)
    private DeliveryState deliveryState;
}