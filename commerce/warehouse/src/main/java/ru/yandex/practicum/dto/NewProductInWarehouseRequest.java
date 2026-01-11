package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class NewProductInWarehouseRequest {

    @NotNull
    private UUID productId;

    private boolean fragile;

    @NotNull(message = "Объект dimension не может быть null")
    private DimensionDto dimension;

    @DecimalMin(value = "1.0")
    private Double weight;
}
