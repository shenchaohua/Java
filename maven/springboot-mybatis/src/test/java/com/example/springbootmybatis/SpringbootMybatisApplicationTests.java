package com.example.springbootmybatis;

import com.example.dao.AccountDao;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootMybatisApplicationTests {

    @Autowired
    public AccountDao accountDao;

    @Test
    void contextLoads() {
        System.out.println(accountDao.getOneAccount(1));
    }

}
