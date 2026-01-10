package ru.yandex.practicum.dto;

import java.util.Map;
import java.util.UUID;

public class ShoppingCartDto {

   private String cartId;

   private Map<UUID, Integer> products;
}
