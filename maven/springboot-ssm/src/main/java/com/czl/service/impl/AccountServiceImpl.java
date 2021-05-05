package com.czl.service.impl;

import com.czl.dao.AccountDao;
import com.czl.domain.Account;
import com.czl.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public List<Account> findAll() {
        return accountDao.findAll();
    }

    @Override
    public void save(Account account) {
        accountDao.save(account);
    }

    @Override
    public Account findById(Integer id) {
        return accountDao.findById(id);
    }

    @Override
    public void update(Account account) {
        accountDao.update(account);
    }
    @Override
    public void deleteBatch(Integer[] ids) {
        accountDao.deleteBatch(ids);
    }
}
