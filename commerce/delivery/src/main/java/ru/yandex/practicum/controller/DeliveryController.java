package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.model.delivery.Delivery;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    //TODO
    @PutMapping
    public DeliveryDto createNewDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        log.debug("POST /api/v1/delivery - delivery dto: {}", deliveryDto);
        Delivery delivery = DeliveryMapper

    }

    //TODO
    @PostMapping("/successful")
    public void deliverySuccessful(@RequestBody @Valid UUID deliveryId) {
    }

    //TODO
    @PostMapping("/picked")
    public void deliveryPicked(@RequestBody @Valid UUID deliveryId) {
    }

    //TODO
    @PostMapping("/failed")
    public void deliveryFailed(@RequestBody @Valid UUID deliveryId) {
    }

    //TODO
    @PostMapping("/cost")
    public void deliveryCost(@RequestBody @Valid UUID deliveryId) {
    }
}
