package ru.yandex.practicum.model;

import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pageable {

    @Min(value = 0, message = "Значение page не может быть меньше 0")
    private Integer page;

    @Min(value = 0, message = "Значение size не может быть меньше 1")
    private Integer size;

    private List<String> sort;
}
