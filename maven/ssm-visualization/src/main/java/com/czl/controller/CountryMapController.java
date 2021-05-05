package com.czl.controller;

import com.czl.common.ServerResponse;
import com.czl.service.CountryMapService;
import com.czl.vo.ProvinceVo;
import com.czl.vo.SalaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/map")
public class CountryMapController {

    @Autowired
    private CountryMapService service;

    /**
     * 统计各省的招聘人数
     */
    @RequestMapping(value = "mapData.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<ProvinceVo>>  countProvinceData(){
        ServerResponse<List<ProvinceVo>> response = service.getMapData();
        return response;
    }

    /**
     * 统计前10的平均工资
     */
    @RequestMapping(value = "salaryData.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Map<String,Object>>  countSalaryData(){
        ServerResponse<Map<String,Object>> response = service.getSalaryData();
        return response;
    }
}