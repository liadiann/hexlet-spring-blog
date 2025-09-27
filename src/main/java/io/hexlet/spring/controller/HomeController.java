package io.hexlet.spring.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;;

@RestController
public class HomeController {
    @Value("${app.welcome-message}")
    private String welcomeMessage;
    @GetMapping("/")
    public String home() {
        return "Добро пожаловать в Hexlet Spring Blog!";
    }

    @GetMapping("/about")
    public String about() {
        return "Это простой блог на Spring. Я только учусь";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
}
