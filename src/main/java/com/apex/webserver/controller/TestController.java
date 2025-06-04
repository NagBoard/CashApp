package com.apex.webserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final String MESSAGE = "Hello World!";

    @GetMapping("/message")
    public String getMessage() {
        return MESSAGE;
    }

    @PostMapping("/message")
    public String postMessage(String message) {
        return message;
    }
}
