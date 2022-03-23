package com.example.springbootwebdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReturnController {

    @GetMapping("/return")
    public Person get() {
        return new Person();
    }

    class Person {
        String name;
    }
}
