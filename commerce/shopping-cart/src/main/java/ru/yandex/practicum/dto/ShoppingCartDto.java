package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ShoppingCartDto {

    private UUID shoppingCartId;
    private Map<UUID, Integer> products;
}
