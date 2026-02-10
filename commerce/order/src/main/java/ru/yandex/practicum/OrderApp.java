package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.feign.config.FeignConfig;
import ru.yandex.practicum.feign.payment.FeignClientPayment;
import ru.yandex.practicum.feign.warehouse.FeignClientWarehouse;

@EnableFeignClients(clients = {FeignClientWarehouse.class, FeignClientPayment.class})
@EnableDiscoveryClient
@SpringBootApplication
@ConfigurationPropertiesScan
@Import(FeignConfig.class)
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }
}
