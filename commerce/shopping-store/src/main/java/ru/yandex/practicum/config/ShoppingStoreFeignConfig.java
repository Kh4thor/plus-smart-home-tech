package ru.yandex.practicum.config;

import feign.Feign;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.feign.config.decoder.FeignErrorDecoder;

@Configuration
@EnableFeignClients(basePackages = {"ru.yandex.practicum.interaction.api"})
public class ShoppingStoreFeignConfig {
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .errorDecoder(new FeignErrorDecoder());
    }
}
