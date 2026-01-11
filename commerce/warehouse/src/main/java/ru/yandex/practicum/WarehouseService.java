package ru.yandex.practicum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.repository.WarehouseRepository;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public void registerProduct(@Valid NewProductInWarehouseRequest newProduct) {

    }

    public void checkProductQuantity(@Valid ShoppingCartDto shoppingCartDto) {

    }

    public void addProduct(@Valid AddProductToWarehouseRequest request) {

    }

    public void getAddress(@Valid AddressDto addressDto) {

    }
}
