package com.czl.cloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class HystrixPaymentController {

    @GetMapping("/payment/ok/get/{id}")
    public String paymentInfoOk(@PathVariable Long id) {
        return "ok" + id;
    }

    @HystrixCommand(fallbackMethod="paymentInfoTimeOutHandler",
            commandProperties={@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="3000")})
    @GetMapping("/payment/timeout/get/{id}")
    public String paymentInfoTimeout(@PathVariable Long id) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "timeout " + id;
    }

    public String paymentInfoTimeOutHandler(Long id) {
        return "fallback " + id;
    }

    @HystrixCommand(fallbackMethod = "paymentCircuitBreakerFallback",
        commandProperties = {
            @HystrixProperty(name="circuitBreaker.enabled", value = "true"),
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value = "60"),
        })
    @GetMapping("/payment/circuit/{id}")
    public String paymentCircuitBreaker(@PathVariable Long id) {
        if (id<0) {
            throw new RuntimeException("id不能小于0");
        }
        return UUID.randomUUID().toString();
    }

    public String paymentCircuitBreakerFallback(Long id) {
        return "稍后重试";
    }
}
