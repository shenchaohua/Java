package com.czl.cloud.service.impl;

import com.czl.cloud.dao.PaymentDao;
import com.czl.cloud.entity.Payment;
import com.czl.cloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    public PaymentDao paymentDao;

    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }

    public int create(Payment payment) {
        return paymentDao.create(payment);
    }
}
