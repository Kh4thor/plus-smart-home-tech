package ru.yandex.practicum.telemetry.collector.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "ru.practicum")
public class MainCollector {
    public static void main(String[] args) {
        SpringApplication.run(MainCollector.class,args);
    }
}