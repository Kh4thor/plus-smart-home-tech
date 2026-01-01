package ru.yandex.practicum.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.model.enums.ProductCategory;
import ru.yandex.practicum.model.enums.ProductState;
import ru.yandex.practicum.model.enums.QuantityState;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", nullable = false, updatable = false)
    private UUID productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_src")
    private String imageSrc;

    @Column(name = "quantity_state", nullable = false)
    private QuantityState quantityState;

    @Column(name = "product_state", nullable = false)
    private ProductState productState;

    @Column(name = "product_category")
    private ProductCategory productCategory;

    @Column(name = "price")
    private Integer price;
}


