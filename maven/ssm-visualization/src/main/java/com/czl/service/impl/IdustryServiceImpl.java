package com.czl.service.impl;

import com.czl.common.Const;
import com.czl.common.ServerResponse;
import com.czl.dao.IdustryDao;
import com.czl.service.IdustryService;
import com.czl.vo.ConverterVo;
import com.czl.vo.IndustryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IdustryServiceImpl implements IdustryService {

    @Autowired
    private IdustryDao mapper;

    /**
     * 统计各个城市招聘情况
     * @return
     */
    @Override
    public ServerResponse<Map<String, Object>> countCityData() {
        //1.查询各个城市招聘情况 , 创建一个对象封装查询的数据 list
        List<ConverterVo> cityPositionList = mapper.countCityPosition();

        //2.创建一个Map<String,Object> 存放总数据
        Map<String , Object> map = new HashMap<>();

        //3.创建3个List集合  cityNamesList 存放城市名称  ; thridList 前三名城市数据  dataList 存放是个城市数据
        List<String> cityNamesList = new ArrayList<>();
        List<ConverterVo> thridList = new ArrayList<>();
        List<ConverterVo> dataList = new ArrayList<>();

        //4.循环list ,给cityNamesList   thridList  dataList  封装数据
        for (ConverterVo converterVo : cityPositionList) {
            //设置城市名称
            cityNamesList.add(converterVo.getName());
            //设置thridList
            if(thridList.size() < 3 ){
                thridList.add(converterVo);
            }
            //设置dataList
            dataList.add(converterVo);
        }

        //5.把3个list的数据村道map中
        map.put("cityNamesList",cityNamesList);
        map.put("thridList",thridList);
        map.put("dataList",dataList);

        //6.把map中的数据放到serverresponse中返回
        return ServerResponse.createBySuccess(map);
    }

    /**
     * 2.热门行业top10
     */
    @Override
    public ServerResponse<Map<String, Object>> getIndustryTop10() {
        //1.查询最热门的10个行业  List<IndustryVo>
        List<IndustryVo> list =  mapper.countIndustryTop();

        //2.创建map 封装所有数据
        Map<String,Object> map = new HashMap<>();

        //3.创建两个list  namesList 放行业名称   countList 行业数量
        List<String> industryNames = new ArrayList<>();
        List<Long> countList = new ArrayList<>();

        //4.循环给list设置数据
        for (IndustryVo industryVo : list) {
            industryNames.add(industryVo.getPositionName());
            countList.add(industryVo.getNum());
        }

        //5.list存在map中
        map.put("names",industryNames);
        map.put("count",countList);

        //6.map放到ServerResponse
        return ServerResponse.createBySuccess(map);
    }

    /**
     * 统计 热门行业(销售 服务业 生产制造) 在2020-6-4 到2020-6-9 招聘信息对比
     * @return
     */
    @Override
    public ServerResponse<Map<String, Object>> industryCompare() {
        //1.创建Map集合,封装最终的数据
        Map<String,Object> map = new HashMap<>();
        //3.循环行业
        for(String industryName : Const.INDUSTRY){
            //创建List<Long> 记录行业每天招聘情况
            List<Long> countNumList = new ArrayList<>();
            List<IndustryVo> dataList = mapper.getIndustryData(industryName, Const.START_DATE,Const.END_DATE);

            //4.循环dataList ,目的是给countNumList 设置数据
            for (IndustryVo industryVo : dataList) {
                countNumList.add(industryVo.getNum());
            }

            //5.判断行业,给map中设置值
            if(industryName.equals(Const.INDUSTRY[0])){
                map.put("sell",countNumList);
            }else if(industryName.equals(Const.INDUSTRY[1])){
                map.put("server",countNumList);
            }else if(industryName.equals(Const.INDUSTRY[2])){
                map.put("pro",countNumList);
            }
        }
        map.put("dateTime",Const.DATE_TIME);
        map.put("industryNameList",Const.INDUSTRY);

        return ServerResponse.createBySuccess(map);
    }

}
