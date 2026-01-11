package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DimensionDto {

    @NotNull(message = "Поле width не может быть null")
    @DecimalMin(value = "1.0", message = "Значение поля width не может быть меньше 1.0")
    private Double width;

    @NotNull(message = "Поле height не может быть null")
    @DecimalMin(value = "1.0", message = "Значение поля width не может быть меньше 1.0")
    private Double height;

    @NotNull(message = "Поле depth не может быть null")
    @DecimalMin(value = "1.0", message = "Значение поля width не может быть меньше 1.0")
    private Double depth;
}
