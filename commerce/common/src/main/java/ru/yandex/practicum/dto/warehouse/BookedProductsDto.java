package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookedProductsDto {

    @NotNull(message = "Поле deliveryWeight не может быть null")
    @DecimalMin(value = "1.0")
    private Double deliveryWeight;

    @NotNull(message = "Поле deliveryVolume не может быть null")
    @DecimalMin(value = "1.0")
    private Double deliveryVolume;

    private boolean fragile;
}
