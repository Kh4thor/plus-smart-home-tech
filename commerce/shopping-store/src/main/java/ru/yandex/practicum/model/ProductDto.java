package ru.yandex.practicum.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.model.enums.ProductCategory;
import ru.yandex.practicum.model.enums.ProductState;
import ru.yandex.practicum.model.enums.QuantityState;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private UUID productId;

    @NotNull(message = "Поле productName не может быть null")
    private String productName;

    @NotNull(message = "Поле description не может быть null")
    private String description;

    private String imageSrc;

    @NotNull(message = "Поле quantityState не может быть null")
    private QuantityState quantityState;

    @NotNull(message = "Поле productState не может быть null")
    private ProductState productState;

    private ProductCategory productCategory;

    @NotNull
    @DecimalMin("1.00")
    @Digits(integer = 10, fraction = 2)
    private Double price;
}
