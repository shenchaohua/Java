package com.czl.controller;

import com.czl.common.ServerResponse;
import com.czl.service.EduService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/edu")
public class EduController {

    @Autowired
    private EduService service;

    /**
     * 获取不同学历下的岗位数量
     */
    @RequestMapping("/eduCount")
    @ResponseBody
    public ServerResponse<Map<String,Object>> getEduCount(){
        ServerResponse<Map<String,Object>> response = service.getEduData();
        return response;
    }
}
