package com.czl.test;

import com.czl.dao.AccountDao;
import com.czl.domain.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AccountTest {
    @Test
    public void test() throws IOException {
        InputStream is = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        AccountDao accountDao = sqlSession.getMapper(AccountDao.class);
        List<Account> list = accountDao.findAll();
        for (Account account : list) {
            System.out.println(account);
        }
        sqlSession.close();
    }
}
