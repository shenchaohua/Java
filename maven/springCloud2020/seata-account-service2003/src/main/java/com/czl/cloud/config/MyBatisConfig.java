package com.czl.cloud.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan({"com.czl.cloud.dao"})
public class MyBatisConfig {
}
