package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AddProductToWarehouseRequest {

    private UUID productId;

    @NotNull(message = "Поле quantity не может быть null")
    private Integer quantity;
}
