package ru.yandex.practicum.dto.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.enums.delivery.DeliveryState;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

    @NotNull(message = "Delivery id не может быть null")
    private UUID deliveryId;

    private AddressDto fromAddress;

    private AddressDto toAddress;

    @NotNull(message = "Order id не может быть null")
    private UUID orderId;

    @NotNull(message = "Delivery state не может быть null")
    private DeliveryState state;
}
