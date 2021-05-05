package com.czl.dao.impl;

import com.czl.dao.AccountDao;
import com.czl.domain.Account;
import com.czl.utils.ConnectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class AccountDaoImpl implements AccountDao {
    @Autowired
    private QueryRunner queryRunner;

    @Autowired
    private ConnectionUtils connectionUtils;

    @Override
    public void out(String outUser, int money) {
        try {
            queryRunner.update(connectionUtils.getThreadConnection(), "update account set money=money-? where name=?", money, outUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void in(String inUser, int money) {
        try {
            queryRunner.update(connectionUtils.getThreadConnection(), "update account set money=money+? where name=?", money, inUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
