package com.czl.service;

import com.czl.Model.User;
import com.czl.common.ServerResponse;
import com.czl.vo.UserVo;
import com.github.pagehelper.PageInfo;

public interface UserService {

    public ServerResponse<User> login(String username, String password);

    ServerResponse<PageInfo> getUsers(int pageNum, int pageSize);

    ServerResponse<String> add(User user);
    ServerResponse<String> update(UserVo user);

    ServerResponse<String> deleteByPrimary(int id);
}
