package com.czl.test;

import com.czl.dao.IUserDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
    @Test
    public void test1(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IUserDao iUserDaoImpl = (IUserDao)context.getBean("userDao");
        iUserDaoImpl.save();
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IUserDao iUserDaoImpl = (IUserDao)context.getBean("userDao");
        iUserDaoImpl.save();
    }
}
