package com;

import com.example.dao.AccountDao;
import com.example.domain.Account;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.dao")
public class SprintBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprintBootDemoApplication.class, args);
    }



}
