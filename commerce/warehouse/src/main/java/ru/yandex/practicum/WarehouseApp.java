package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.feign.delivery.FeignClientDelivery;
import ru.yandex.practicum.feign.order.FeignClientOrder;

@EnableFeignClients(clients = {FeignClientOrder.class, FeignClientDelivery.class})
@EnableDiscoveryClient
@SpringBootApplication
@ConfigurationPropertiesScan
public class WarehouseApp {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseApp.class, args);
    }
}



