package com.czl.service.impl;

import com.czl.dao.impl.AccountDaoImpl;
import com.czl.service.AccountService;
import com.czl.utils.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDaoImpl accountDao;

    @Autowired
    private TransactionManager transactionManager;

    @Override
    public void transfer(String outUser, String inUser, int money) {
            // 2.业务操作
            accountDao.out(outUser, money);
//            int i = 1 / 0;
            accountDao.in(inUser, money);
    }
}
