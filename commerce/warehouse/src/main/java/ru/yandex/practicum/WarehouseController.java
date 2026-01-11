package ru.yandex.practicum;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.NewProductInWarehouseRequest;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void addProduct(@RequestBody @Valid NewProductInWarehouseRequest newProduct) {

    }
}
