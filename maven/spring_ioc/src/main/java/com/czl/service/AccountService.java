package com.czl.service;

public interface AccountService {
    public void transfer(String inUser,String outUser,int money);

    public static void main(String[] args) {
        System.out.println(AccountService.class.getInterfaces());
    }
}
