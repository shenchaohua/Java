package com.czl.cloud.controller;

import com.czl.cloud.entity.CommonResult;
import com.czl.cloud.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class OrderController {

    public static final String URL = "http://CLOUD-PAYMENT-SERVICE";

    @Resource
    public RestTemplate restTemplate;

    @GetMapping("/consumer/payment/create")
    public CommonResult create(Payment payment) {
        return restTemplate.postForObject(URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable Long id) {
        return restTemplate.getForObject(URL + "/payment/get/"+id, CommonResult.class);
    }
}
