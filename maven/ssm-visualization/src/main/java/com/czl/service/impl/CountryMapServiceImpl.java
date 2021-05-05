package com.czl.service.impl;

import com.czl.common.CityProvinceConverter;
import com.czl.common.ServerResponse;
import com.czl.dao.CountryMapDao;
import com.czl.service.CountryMapService;
import com.czl.vo.CityVo;
import com.czl.vo.ProvinceVo;
import com.czl.vo.SalaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CountryMapServiceImpl  implements CountryMapService {

    @Autowired
    private CountryMapDao mapper;

    @Override
    public ServerResponse<List<ProvinceVo>> getMapData() {
        //1.查询各个省份有哪些城市  List<ProvinceVo>   没有城市招聘数量0
        List<ProvinceVo>  provinceVoList = mapper.queryProvinceCity();

        //2.查询每个城市招聘数量 List<CityVo>  有城市招聘数量
        List<CityVo>  cityList = mapper.queryCityNum();

        //3.转换类,传递 List<ProvinceVo>    List<CityVo>  --->  List<ProvinceVo> 结果中
        List<ProvinceVo> provinceVos = CityProvinceConverter.converter(cityList,provinceVoList);

        //4.返回ServerResponse ,把List<ProvinceVo>放到
        return ServerResponse.createBySuccess(provinceVos);
    }

    @Override
    public ServerResponse<Map<String,Object>> getSalaryData() {
        List<SalaryVo> salaryVos = mapper.querySalary();
        Map<String , Object> map = new HashMap<>();
        List<String> names = new ArrayList<>();
        List<Double> salary = new ArrayList<>();
        for (SalaryVo salaryVo : salaryVos) {
            names.add(salaryVo.getName());
            salary.add(salaryVo.getAvgSalary());
        }
        map.put("names",names);
        map.put("count",salary);
        return ServerResponse.createBySuccess(map);
    }
}
