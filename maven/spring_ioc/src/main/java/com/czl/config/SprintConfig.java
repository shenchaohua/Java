package com.czl.config;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.czl")
@Import(DataSourceConfig.class)
public class SprintConfig {
    @Bean("queryRunner")
    public QueryRunner getQueryRunner(@Autowired DataSource dataSource){
        return new QueryRunner(dataSource);
    }
}
