package com.czl.service.impl;

import com.czl.dao.AccountDao;
import com.czl.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;


    @Override
    public int transfer(String inName, String outName, double money) {
        int ret = 1;
        //调用转入
        accountDao.transferIn(inName,money);
//调用转出
        accountDao.transferOut(outName,money);
        ret = 0;
        return ret;
    }
}
