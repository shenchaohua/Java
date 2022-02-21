package com.czl.cloud.service;

import com.czl.cloud.entity.CommonResult;
import com.czl.cloud.entity.Payment;
import com.czl.cloud.service.fallback.FeignFallbackPaymentService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT", fallback = FeignFallbackPaymentService.class)
public interface FeignPaymentService {

    @GetMapping("/payment/ok/get/{id}")
    String getPaymentById(@PathVariable("id") Long id);

    @GetMapping("/payment/timeout/get/{id}")
    String getPaymentTimeoutById(@PathVariable("id") Long id);
}
