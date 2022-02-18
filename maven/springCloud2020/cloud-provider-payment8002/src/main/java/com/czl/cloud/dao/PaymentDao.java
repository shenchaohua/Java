package com.czl.cloud.dao;

import com.czl.cloud.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentDao {

    public Payment getPaymentById(Long id);

    public int create(Payment payment);
}
