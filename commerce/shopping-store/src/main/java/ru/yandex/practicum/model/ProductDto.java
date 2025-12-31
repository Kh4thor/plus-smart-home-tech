package ru.yandex.practicum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.NonNull;


@Getter
@Setter
@RequiredArgsConstructor
public class ProductDto {

    @NonNull(message = "not null")
    private String productId;

    private String productName;

    private String description;

    private String imageSrc;


}
