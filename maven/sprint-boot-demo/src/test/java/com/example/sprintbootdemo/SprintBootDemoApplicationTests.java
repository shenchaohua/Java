package com.example.sprintbootdemo;

import com.example.controller.HelloController;
import com.example.dao.AccountDao;
import com.example.domain.Account;
import com.example.domain.Person;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class SprintBootDemoApplicationTests {

    @Autowired
    private HelloController helloController;

    @Test
    void contextLoads() {
        System.out.println(helloController.hello());

    }

    @Autowired
    private Person person;
    @Test
    void configurationTest() {
        System.out.println(person);
    }

    @Autowired
    private ApplicationContext applicationContext;
    @Test
    public void iocTest() {
        System.out.println(applicationContext.containsBean("myService"));
    }

    @Autowired
    private AccountDao accountDao;

    @Test
    void contextLoads2() {
        Account comment = accountDao.getById(1);
        System.out.println(comment);
    }
}
