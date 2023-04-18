package com.example.oms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloWorldController {
    private static final String template = "Hello, %s!";
    @GetMapping("/hello-world")
    public String helloWorld(@RequestParam(defaultValue = "World") String name) {
        return String.format(template, name);
    }
}
