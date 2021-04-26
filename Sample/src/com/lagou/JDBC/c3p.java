package com.lagou.JDBC;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class c3p {
    public static ComboPooledDataSource dataSource = new ComboPooledDataSource();
    public static DataSource dataSource2;
    public static void main(String[] args) throws Exception {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        Properties p = new Properties();
        InputStream inputStream =
                c3p.class.getClassLoader().getResourceAsStream("druid.properties");
        p.load(inputStream);
        dataSource2 = DruidDataSourceFactory.createDataSource(p);
        Connection connection1 = dataSource2.getConnection();
        System.out.println(connection1);

    }
}
