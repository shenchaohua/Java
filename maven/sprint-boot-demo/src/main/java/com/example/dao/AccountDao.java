package com.example.dao;

import com.example.domain.Account;
import org.apache.ibatis.annotations.Select;

public interface AccountDao {

    @Select("select * from account where id=#{id}")
    public Account getOneAccount(int id);
}
