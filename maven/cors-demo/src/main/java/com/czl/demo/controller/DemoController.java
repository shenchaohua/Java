package com.czl.demo.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class DemoController {

    @RequestMapping(path = "/hello", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @CrossOrigin("*")
    public String hello(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return "hello";
    }

    @RequestMapping(path = "/", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public String hello2(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return "hello";
    }
}
