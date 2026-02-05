package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.model.delivery.Delivery;
import ru.yandex.practicum.service.DeliveryService;
import ru.yandex.practicum.utils.delivery.DeliveryMapper;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto createDeliveryByDto(@RequestBody @Valid DeliveryDto deliveryDto) {
        log.debug("POST /api/v1/delivery - delivery dto: {}", deliveryDto);
        Delivery createdDelivery = deliveryService.createDeliveryByDto(deliveryDto);
        log.info("Created delivery: {}", createdDelivery);
        DeliveryDto createdDeliveryDto = DeliveryMapper.toDeliveryDto(createdDelivery);
        log.info("Created delivery mapped to dto: {}", createdDeliveryDto);
        return createdDeliveryDto;
    }

    @PostMapping("/successful")
    public void successfulDeliveryById(@RequestBody @Valid UUID deliveryId) {
        log.debug("POST /api/v1/delivery/successful - delivery id: {}", deliveryId);
        Delivery delivery = deliveryService.successfulDeliveryById(deliveryId);
        log.info("Successfully delivery: {}", delivery);
    }

    @PostMapping("/picked")
    public void pickedDeliveryById(@RequestBody @Valid UUID deliveryId) {
        log.debug("POST /api/v1/delivery/picked - delivery id: {}", deliveryId);
        Delivery delivery = deliveryService.pickedDeliveryById(deliveryId);
        log.info("Picked delivery: {}", delivery);
    }

    //TODO
    @PostMapping("/failed")
    public void failedDelivery(@RequestBody @Valid UUID deliveryId) {
        log.debug("POST /api/v1/delivery/failed - delivery id: {}", deliveryId);
        Delivery delivery = deliveryService.failedDelivery(deliveryId);
        log.info("Failed delivery: {}", delivery);
    }

    //TODO
    @PostMapping("/cost")
    public Double getDeliveryCostByDeliveryId(@RequestBody @Valid OrderDto orderDto) {
        log.debug("POST /api/v1/delivery/cost - order dto: {}", orderDto);
        Double deliveryCost = deliveryService.getDeliveryCost(orderDto);
        log.info("Calculated delivery cost: {}", deliveryCost);
        return deliveryCost;
    }
}
