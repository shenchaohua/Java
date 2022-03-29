package com.czl.test.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    public String index() {
        return "hello";
    }
}
