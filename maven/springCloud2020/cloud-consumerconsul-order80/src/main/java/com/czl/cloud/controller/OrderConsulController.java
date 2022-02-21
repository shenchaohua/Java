package com.czl.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class OrderConsulController {

    public static final String URL = "http://consul-provider-payment";

    @Resource
    public RestTemplate restTemplate;

    @GetMapping("/consumer/consul/payment")
    public String getPayment() {
        return restTemplate.getForObject(URL + "/consul/payment", String.class);
    }
}
