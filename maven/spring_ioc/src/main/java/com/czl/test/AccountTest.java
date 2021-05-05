package com.czl.test;

import com.czl.Proxy.JdkProxyFactory;
import com.czl.config.SprintConfig;
import com.czl.service.AccountService;
import com.czl.service.impl.AccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SprintConfig.class})
public class AccountTest {
    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private JdkProxyFactory jdkProxyFactory;

    @Test
    public void test(){
//        accountService.transfer("zhangfei","guanyu",200);
        AccountService accountServiceJdkProxy = jdkProxyFactory.createAccountServiceJdkProxy();
        accountServiceJdkProxy.transfer("zhangfei","guanyu",200);
    }
}
