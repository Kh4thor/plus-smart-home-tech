package ru.yandex.practicum.dto.shopping.store;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.enums.shopping.store.QuantityState;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetProductQuantityStateRequest {

    @NotNull(message = "Поле productId не может быть null")
    private UUID productId;

    @NotNull(message = "Поле quantityState не может быть null")
    private QuantityState quantityState;
}
