package ru.yandex.practicum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthorizationController {

    @GetMapping("/public")
    public String getPublicHelloWorld() {
        return "Hello World from public";
    }

    @GetMapping("/private")
    public String getPrivateHelloWorld() {
        return "Hello World from private";
    }
}
