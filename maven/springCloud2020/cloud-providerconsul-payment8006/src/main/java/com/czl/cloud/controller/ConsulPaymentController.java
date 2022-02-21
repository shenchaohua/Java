package com.czl.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ConsulPaymentController {

    @GetMapping("/consul/payment")
    public String getPayment() {
        return "consul payment" + UUID.randomUUID().toString();
    }
}
