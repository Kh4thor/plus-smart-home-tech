package ru.yandex.practicum.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.model.enums.ProductState;
import ru.yandex.practicum.model.enums.QuantityState;

@Getter
@Setter
@Entity
@Table(name = "products")
@RequiredArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name")
    private String productName;

    private String description;

    @Column(name = "image_src")
    private String imageSrc;

    @Column(name = "quantity_state")
    private QuantityState quantityState;

    @Column(name = "product_state")
    private ProductState productState;

    @Column(name = "price")
    private Double price;
}
