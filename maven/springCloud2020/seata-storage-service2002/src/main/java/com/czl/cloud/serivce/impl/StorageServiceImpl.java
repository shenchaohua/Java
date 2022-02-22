package com.czl.cloud.serivce.impl;

import com.czl.cloud.dao.StorageDao;
import com.czl.cloud.serivce.StorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StorageServiceImpl implements StorageService {
    @Resource
    public StorageDao storageDao;
    @Override
    public void decrease(Long productId, Integer count) {
        storageDao.decrease(productId, count);
    }
}
