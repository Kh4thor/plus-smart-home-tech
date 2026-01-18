package ru.yandex.practicum.dto.shopping.store;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private UUID productId;

    @NotBlank(message = "Поле productName не может быть пустым")
    private String productName;

    @NotBlank(message = "Поле description не может быть пустым")
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
