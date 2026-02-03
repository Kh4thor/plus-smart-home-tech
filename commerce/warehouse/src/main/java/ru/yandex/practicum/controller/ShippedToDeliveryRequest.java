package ru.yandex.practicum.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ShippedToDeliveryRequest {
    @NotNull(message = "Поле orderId не может быть null")
    private UUID orderId;

    @NotNull(message = "Поле deliveryId не может быть null")
    private UUID deliveryId;
}
