package com.czl.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface AccountDao {
    //转入钱
    @Update("update account set money=money+#{money} where username=#{name}")
    public void transferIn(@Param("name") String name, @Param("money")double money);
    //转出钱
    @Update("update account set money=money-#{money} where username=#{name}")
    public void transferOut(@Param("name") String name, @Param("money")double money);

}
