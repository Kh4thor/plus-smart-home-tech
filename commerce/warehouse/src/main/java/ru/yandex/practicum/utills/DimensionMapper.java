package ru.yandex.practicum.utills;

import ru.yandex.practicum.dto.DimensionDto;
import ru.yandex.practicum.model.Dimension;

public class DimensionMapper {

    public Dimension toDimension(DimensionDto dimensionDto) {
        if (dimensionDto == null)
            return null;

        return Dimension.builder()
                .depth(dimensionDto.getDepth())
                .width(dimensionDto.getWidth())
                .height(dimensionDto.getHeight())
                .build();
    }

    public DimensionDto toDimensionDto(Dimension dimension) {
        if (dimension == null)
            return null;

        return DimensionDto.builder()
                .depth(dimension.getDepth())
                .width(dimension.getWidth())
                .height(dimension.getHeight())
                .build();
    }
}
