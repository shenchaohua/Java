package com.czl.dao.impl;

import com.czl.dao.IUserDao;

public class IUserDaoImpl implements IUserDao {
    private String name;

    public void setName(String name){
        this.name = name;
        System.out.println(name);
    }

    @Override
    public void save() {
        System.out.println("save");
    }
}
