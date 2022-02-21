package com.czl.cloud.controller;

import com.czl.cloud.entity.CommonResult;
import com.czl.cloud.entity.Payment;
import com.czl.cloud.service.FeignPaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class FeignPaymentController {

    @Resource
    public FeignPaymentService feignPaymentService;

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return feignPaymentService.getPaymentById(id);
    }
}
