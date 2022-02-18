package com.czl.cloud.controller;

import com.czl.cloud.entity.CommonResult;
import com.czl.cloud.entity.Payment;
import com.czl.cloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    @Autowired
    public PaymentService paymentService;

    @Value("${server.port}")
    public String serverPort;

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        if (payment != null) {
            return new CommonResult<>(200, "insert success, serverPort:"+serverPort, payment);
        }else {
            return new CommonResult<>(500, "failed");
        }
    }

    @PostMapping("payment/create")
    public CommonResult create(@RequestBody Payment payment) {
        int  result = paymentService.create(payment);
        if (result>0) {
            return new CommonResult(200, "insert success");
        }else {
            return new CommonResult(400, "insert failed");
        }
    }
}
