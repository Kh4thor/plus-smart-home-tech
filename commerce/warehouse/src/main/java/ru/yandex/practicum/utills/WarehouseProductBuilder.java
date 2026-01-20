package ru.yandex.practicum.utills;

import ru.yandex.practicum.dto.warehouse.DimensionDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.model.Dimension;
import ru.yandex.practicum.model.WarehouseProduct;

public class WarehouseProductBuilder {

    public static WarehouseProduct buildWarehouseProduct(NewProductInWarehouseRequest request) {
        if (request == null)
            return null;

        DimensionDto dimensionDto = request.getDimension();
        Dimension dimension = DimensionMapper.toDimension(dimensionDto);

        return WarehouseProduct.builder()
                .productId(request.getProductId())
                .fragile(request.isFragile())
                .weight(request.getWeight())
                .dimension(dimension)
                .build();
    }
}
