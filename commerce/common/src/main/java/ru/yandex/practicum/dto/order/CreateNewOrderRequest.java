package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewOrderRequest {

    @NotNull (message = "Shopping cart must be not null")
    private ShoppingCartDto shoppingCart;

    @NotNull (message = "Delivery address must be not null")
    private AddressDto deliveryAddress;
}
