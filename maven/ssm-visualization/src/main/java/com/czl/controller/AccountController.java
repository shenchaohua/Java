package com.czl.controller;

import com.czl.common.ResponseCode;
import com.czl.common.ServerResponse;
import com.czl.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService service;

    @RequestMapping("transfer.do")
    @ResponseBody
    public ServerResponse<String> accountTransfer(String inName, String outName, double money){
        int status = service.transfer(inName, outName, money);
//如果执行转账成功
        if(ResponseCode.SUCCESS.getCode() == status){
            return ServerResponse.createBySuccessMessage("转账成功");
        }else{
            return ServerResponse.createByErrorMessage("转账失败");
        }
    }
}
