package com.czl.controller;

import com.czl.Model.User;
import com.czl.common.Const;
import com.czl.common.ResponseCode;
import com.czl.common.ServerResponse;
import com.czl.service.UserService;
import com.czl.vo.UserVo;
import com.github.pagehelper.PageInfo;
import com.mysql.fabric.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
@Transactional
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 1.用户登录
     */
    @RequestMapping(value = "login.do" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password , HttpSession session){
        //1.调用serivce的登录方法 ,会返回一个ServerResponse<User>
        ServerResponse<User> response = userService.login(username,password);
        //2.判断是否登录成功,如果登录成功,把当前用户user放到session中
        if(response.getStatus() == ResponseCode.SUCCESS.getCode()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        //3.返回ServerResponse
        return  response;
    }


    /**
     * 2.分页查找
     */
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo>  list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        ServerResponse<PageInfo> users =  userService.getUsers(pageNum,pageSize);
        return users;
    }

    /**
     * 3.添加用户
     */
    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> add(User user){
        return userService.add(user);
    }

    /**
     * 3.1.更新用户
     */
    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> update(UserVo user){
        System.out.println(user);
        return userService.update(user);
    }

    /**
     * 4.删除用户
     */
    @RequestMapping(value = "deleteUser.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> delete(int id){
        return userService.deleteByPrimary(id);
    }

    /**
     * 5.用户登出
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        try{
            session.removeAttribute(Const.CURRENT_USER);
            return ServerResponse.createBySuccessMessage("注销成功");
        }catch (Exception e){
            return ServerResponse.createByErrorMessage("注销失败");
        }
    }

}
