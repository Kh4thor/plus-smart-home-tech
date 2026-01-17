package ru.yandex.practicum.dto.shopping.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeQuantityDto {

    @NotNull(message = "Значение поля productId не может быть null")
    private UUID productId;

    @NotNull(message = "Значение поля newQuantity не может быть null")
    @Positive(message = "Значение поля newQuantity должно быть положительным")
    private Integer newQuantity;
}
