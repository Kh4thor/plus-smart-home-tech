package ru.yandex.practicum.model.delivery;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.enums.delivery.DeliveryState;
import ru.yandex.practicum.model.warehouse.Address;

import java.util.UUID;

@Entity
@Getter
@Setter
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
    private UUID orderId;

    @Column(name = "delivery_state")
    @Enumerated(value = EnumType.STRING)
    private DeliveryState state;
}