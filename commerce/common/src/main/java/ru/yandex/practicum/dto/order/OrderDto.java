package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.order.OrderState;

import java.util.Map;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    @NotNull(message = "Order id не может быть null")
    private UUID orderId;

    private UUID shoppingCartId;

    @NotNull(message = "Список products не может быть null")
    @NotEmpty(message = "Список товаров не может быть пустым")
    private Map<UUID, Integer> products;

    private UUID paymentId;

    private UUID deliveryId;

    @Builder.Default
    private OrderState state = OrderState.NEW;

    @Builder.Default
    @PositiveOrZero(message = "Delivery weight не может быть отрицательным")
    private double deliveryWeight = 0.0;

    @Builder.Default
    @PositiveOrZero(message = "Delivery volume не может быть отрицательным")
    private double deliveryVolume = 0.0;

    private boolean fragile;

    @Builder.Default
    @PositiveOrZero(message = "Total price не может быть отрицательным")
    private double totalPrice = 0.0;

    @Builder.Default
    @PositiveOrZero(message = "Delivery price не может быть отрицательным")
    private double deliveryPrice = 0.0;

    @Builder.Default
    @PositiveOrZero(message = "Product price не может быть отрицательным")
    private double productPrice = 0.0;
}
