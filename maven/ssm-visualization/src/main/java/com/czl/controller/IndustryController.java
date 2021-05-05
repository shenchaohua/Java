package com.czl.controller;


import com.czl.common.ServerResponse;
import com.czl.service.IdustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/industry")
public class IndustryController {

    @Autowired
    private IdustryService iIdustryService;


    /**
     * 1.统计个各个城市招聘人数情况
     * @return
     */
    @RequestMapping("countCityPosition.do")
    @ResponseBody
    public ServerResponse<Map<String,Object>> countCityNums(){
       ServerResponse<Map<String,Object>> response =  iIdustryService.countCityData();
        return response;
    }

    /**
     * 2.热门行业top10
     */
    @RequestMapping(value = "industryTop.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Map<String,Object>> getIndustryTop(){
        ServerResponse<Map<String,Object>> response =  iIdustryService.getIndustryTop10();
        return response;
    }

    /**
     * 3.热门行业对比图
     */
    @RequestMapping(value = "industryCompare.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Map<String,Object>> getIndustryCompare(){
        ServerResponse<Map<String,Object>> response =  iIdustryService.industryCompare();
        return response;
    }
}
