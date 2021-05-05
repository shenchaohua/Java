package com.czl.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
@Component
public class TransactionManager {
    @Autowired
    private ConnectionUtils connectionUtils;
    public void beginTransaction() {
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void commit() {
        try {connectionUtils.getThreadConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void rollback() {
        try {
            connectionUtils.getThreadConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void release() {
        try {
            connectionUtils.getThreadConnection().setAutoCommit(true); // 改回自
            connectionUtils.getThreadConnection().close();// 归还到连接池
            connectionUtils.removeThreadConnection();// 解除线程绑定
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
