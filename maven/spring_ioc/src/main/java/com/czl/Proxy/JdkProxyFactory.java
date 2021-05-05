package com.czl.Proxy;

import com.czl.service.AccountService;
import com.czl.utils.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class JdkProxyFactory {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionManager transactionManager;
    public AccountService createAccountServiceJdkProxy() {
        AccountService accountServiceProxy = (AccountService)Proxy.newProxyInstance(accountService.getClass().getClassLoader(),
                accountService.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {
                        Object result = null;
                        try {
                            // 1.开启事务
                            transactionManager.beginTransaction();
                            // 2.业务操作
                            result = method.invoke(accountService, args);
                            // 3.提交事务
                            transactionManager.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 4.回滚事务
                            transactionManager.rollback();
                        } finally {
                            // 5.释放资源
                            transactionManager.release();
                        }
                        return result;
                                    }
                                });
        return accountServiceProxy;
    }
}
