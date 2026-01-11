package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Dimension {
    private Double width;
    private Double height;
    private Double depth;
}
