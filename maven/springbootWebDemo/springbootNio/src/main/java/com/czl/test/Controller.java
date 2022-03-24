package com.czl.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String get() {
        System.out.println("====");
        String s = restTemplate.getForObject("http://localhost:8080/return", String.class);
        return s;
    }
}
