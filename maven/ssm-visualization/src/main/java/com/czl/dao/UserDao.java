package com.czl.dao;

import com.czl.Model.User;
import com.czl.vo.UserVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserDao {

    /**
     * 根据用户名查找用户是否存在
     * @param username
     * @return
     */
    @Select("select count(1) from user where username=#{username}")
    int checkUserName(String username);

    /**
     * 用户登录
     * @param username
     * @param md5Password
     * @return
     */
    @Select("select * from user where username=#{username} and password=#{password}")
    User selectLogin(@Param("username") String username, @Param("password") String md5Password);

    @Select("select * from user order by id asc")
    List<User> userList();

    @Select("select count(1) from user where email=#{email}")
    int checkEmail(String str);

    @Insert("insert into user (id,username,password,email,phone,create_time,update_time) values(#{id},#{username},#{password},#{email},#{phone},now(),now())")
    int insert(User user);

    @Update("update user set username=#{username},email=#{email},phone=#{phone} where id=#{oldUserId}")
    int update(UserVo uservo);

    @Delete("delete from user where id=#{id}")
    int deleteByPrimaryKey(int id);

    @Select("select * from user where id=#{id}")
    User selectByPrimaryKey(int userId);
}
