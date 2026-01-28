package ru.yandex.practicum.feign.payment;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.dto.order.OrderDto;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface FeignClientPayment {

    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    public Double getTotalCostOfPayment(@Valid @RequestBody OrderDto orderDto);
}