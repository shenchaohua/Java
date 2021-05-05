package com.czl.test;

import com.czl.dao.CountryMapDao;
import com.czl.service.CountryMapService;
import com.czl.service.EduService;
import com.czl.service.IdustryService;
import com.czl.service.UserService;
import com.czl.service.impl.EduServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/*.xml")
public class springTest {
    @Autowired
    private CountryMapDao service;
    @Test
    public void test(){
        System.out.println(service.querySalary());
    }
}
