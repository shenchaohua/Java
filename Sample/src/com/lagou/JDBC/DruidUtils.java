package com.example.util;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DruidUtils {
    public static DataSource dataSource;

    static{
        try {
            //3.创建属性集对象
            Properties p = new Properties();
            //4.加载配置文件 Druid 连接池不能够主动加载配置文件 ,需要指定文件
            InputStream inputStream =
                    DruidUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            //5. 使用Properties对象的 load方法 从字节流中读取配置信息
            p.load(inputStream);
            //6. 通过工厂类获取连接池对象
            dataSource = DruidDataSourceFactory.createDataSource(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取连接的方法
    public static Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //释放资源
    public static void close(Connection con, Statement statement){
        if(con != null && statement != null){
            try {
                statement.close();
                //归还连接
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(Connection con, Statement statement, ResultSet resultSet){
        if(con != null && statement != null && resultSet != null){
            try {
                resultSet.close();
                statement.close();
                //归还连接
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static DataSource getDataSource(){
        return dataSource;
    }

    public static void main(String[] args) {
        DruidUtils.getConnection();
    }
}
