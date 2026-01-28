package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.delivery.DeliveryDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
public class DeliveryController {


    @PutMapping
    public DeliveryDto createNewDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        return deliveryDto;
    }

    @PostMapping("/successful")
    public void deliverySuccessful(@RequestBody @Valid UUID deliveryId) {
    }

    @PostMapping("/picked")
    public void deliveryPicked(@RequestBody @Valid UUID deliveryId) {
    }

    @PostMapping("/failed")
    public void deliveryFailed(@RequestBody @Valid UUID deliveryId) {
    }

    @PostMapping("/cost")
    public void deliveryCost(@RequestBody @Valid UUID deliveryId) {
    }
}
