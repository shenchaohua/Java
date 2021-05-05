package com.czl.dao;

import com.czl.domain.Account;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AccountDao {
    @Select("select * from account")
    public List<Account> findAll();

    @Insert("insert into account (name,money) values(#{name},#{money})")
    void save(Account account);

    @Select("select * from account where id=#{id}")
    Account findById(Integer id);

    @Update("update account set name=#{name},money=#{money} where id=#{id}")
    void update(Account account);

    @Delete("delete from account where id in (#{ids})")
    void deleteBatch(Integer[] ids);
}
