package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "warehouse_products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "fragile")
    private boolean fragile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dimension_id", referencedColumnName = "dimension_id", unique = true)
    private Dimension dimension;
}
