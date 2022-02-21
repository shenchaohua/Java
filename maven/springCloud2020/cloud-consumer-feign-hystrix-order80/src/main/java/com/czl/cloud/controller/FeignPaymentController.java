package com.czl.cloud.controller;

import com.czl.cloud.entity.CommonResult;
import com.czl.cloud.entity.Payment;
import com.czl.cloud.service.FeignPaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
//@DefaultProperties(defaultFallback = "defaultHandler")
public class FeignPaymentController {

    @Resource
    public FeignPaymentService feignPaymentService;

    @GetMapping("/consumer/payment/timeout/get/{id}")
//    @HystrixCommand(
////            fallbackMethod = "paymentTimeOutFallbackMethod",
//            commandProperties={@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="3000")})
    public String getPaymentById(@PathVariable("id") Long id) {
        return feignPaymentService.getPaymentTimeoutById(id);
    }

    public String paymentTimeOutFallbackMethod(Long id) {
        return "order fallback " + id;
    }

    public String defaultHandler() {
        return "default fallback";
    }
}
