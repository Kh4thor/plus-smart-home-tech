package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.feign.order.FeignClientOrder;
import ru.yandex.practicum.feign.shopping.store.FeignClientShoppingStore;

@EnableFeignClients(clients = {FeignClientOrder.class, FeignClientShoppingStore.class})
@EnableDiscoveryClient
@SpringBootApplication
@ConfigurationPropertiesScan
public class PaymentApp {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApp.class, args);
    }
}
