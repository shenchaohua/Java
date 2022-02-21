package com.czl.cloud.service;

import org.springframework.stereotype.Component;

/**
 * @auther zzyy
 * @create 2020-02-25 18:30
 */
@Component
public class PaymentFallbackService implements PaymentService
{
    @Override
    public String getPayment(Long id)
    {
        return "fallback " + id;
    }
}
