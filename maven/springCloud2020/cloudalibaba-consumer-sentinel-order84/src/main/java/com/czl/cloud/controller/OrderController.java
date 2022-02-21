package com.czl.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.czl.cloud.entity.CommonResult;
import com.czl.cloud.entity.Payment;
import com.czl.cloud.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class OrderController {

    public static final String URL = "http://nacos-payment-provider";

    @Resource
    public RestTemplate restTemplate;

    @GetMapping("/consumer/payment/sentinel/{id}")
    @SentinelResource(value = "fallback", fallback = "fallbackHandler")
    public String getPayment(@PathVariable Long id) {
        if (id==4) {
            throw new RuntimeException("error");
        }
        return restTemplate.getForObject(URL + "/payment/sentinel/" + id, String.class);
    }

    public String fallbackHandler(@PathVariable Long id, Throwable e) {
        return "fallback " + e.getMessage();
    }

    //==================OpenFeign
    @Resource
    private PaymentService paymentService;

    @GetMapping(value = "/consumer/openfeign/sentinel/{id}")
    @SentinelResource(value = "fallback", fallback = "fallbackHandler")
    public String paymentSQL(@PathVariable("id") Long id)
    {
        int age =10/0;
        return paymentService.getPayment(id);
    }
}
