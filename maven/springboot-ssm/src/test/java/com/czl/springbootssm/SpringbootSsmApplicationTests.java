package com.czl.springbootssm;

import com.czl.domain.Account;
import com.czl.service.AccountService;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootSsmApplicationTests {

    @Autowired
    private AccountService accountService;

    @Test
    void contextLoads() {
        List<Account> all = accountService.findAll();
        System.out.println(all);
    }

}
