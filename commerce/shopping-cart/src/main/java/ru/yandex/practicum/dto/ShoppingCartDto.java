package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 1)
    private Map<UUID, Integer> products;
}
