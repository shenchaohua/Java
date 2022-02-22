package com.czl.cloud.service;

import com.czl.cloud.model.Order;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrderService {

    void create(Order order);

//    void update(@PathVariable Long userId, @PathVariable Integer status);
}
