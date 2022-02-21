package com.czl.cloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @auther zzyy
 * @create 2020-02-25 18:15
 */
@FeignClient(value = "nacos-payment-provider")
public interface PaymentService
{
    @GetMapping(value = "/payment/sentinel/{id}")
    String getPayment(@PathVariable("id") Long id);
}
