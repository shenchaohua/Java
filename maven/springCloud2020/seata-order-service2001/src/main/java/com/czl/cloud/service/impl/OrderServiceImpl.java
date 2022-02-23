package com.czl.cloud.service.impl;

import com.czl.cloud.dao.OrderDao;
import com.czl.cloud.model.Order;
import com.czl.cloud.service.AccountService;
import com.czl.cloud.service.OrderService;
import com.czl.cloud.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    public OrderDao orderDao;

    @Resource
    public StorageService storageService;

    @Resource
    public AccountService accountService;

    @Override
    @GlobalTransactional(name = "create-order", rollbackFor = Exception.class)
    public void create(Order order) {
        log.info("==开始新建订单");
        orderDao.create(order);
        log.info("==订单新建完成");

        log.info("==开始扣减库存");
        storageService.decrease(order.getProductId(), order.getCount());
        log.info("==完成扣减库存");

        log.info("==开始扣减金钱");
        accountService.decrease(order.getUserId(), order.getMoney());
        log.info("==完成扣减金钱");

        orderDao.update(order.getUserId(), 0);
        log.info("==订单更新完成");

    }
}
