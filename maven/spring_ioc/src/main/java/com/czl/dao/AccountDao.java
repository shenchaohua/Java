package com.czl.dao;

public interface AccountDao {
    public void in(String accountName,int money);
    public void out(String accountName,int money);
}
