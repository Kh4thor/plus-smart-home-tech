package ru.yandex.practicum.dto.shopping.cart;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {

    private UUID shoppingCartId;

    @NotNull(message = "Список products не может быть null")
    @NotEmpty(message = "Список товаров не может быть пустым")
    private Map<UUID, Integer> products;
}
