package com.czl.cloud.service;


import com.czl.cloud.entity.Payment;

public interface PaymentService {
    Payment getPaymentById(Long id);

    int create(Payment payment);
}
