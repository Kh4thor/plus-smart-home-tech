package ru.yandex.practicum.model;

import ru.yandex.practicum.dto.DimensionDto;

import java.util.UUID;

public class NewProductInWarehouseRequest {

    private UUID productId;
    private boolean fragile;
    private DimensionDto dimension;


}
