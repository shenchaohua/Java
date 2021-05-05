package com.czl.service.impl;

import com.czl.common.ServerResponse;
import com.czl.dao.EduDao;
import com.czl.service.EduService;
import com.czl.vo.ConverterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EduServiceImpl implements EduService {

    @Autowired
    private EduDao mapper;

    /**
     * 统计不同学历对应的岗位数量
     * @return
     */
    @Override
    public ServerResponse<Map<String, Object>> getEduData() {

        //1.创建一个Map集合存放数据
        Map<String,Object> dataMap = new HashMap<>();
        //2.查询数据库, List<ConverterVo>
        List<ConverterVo> list = mapper.getEdu();

        //3.创建两个List集合  namesList 存放名称集合    countList 存放不同的学历招聘个数
        List<String> namesList = new ArrayList<>();
        List<String> countList = new ArrayList<>();

        //4.循环List<ConverterVo> ; 把数据给nameList countList设置
        for (ConverterVo vo : list) {
            namesList.add(vo.getName());
            countList.add(vo.getValue());
        }

        //5.把3个list存放到map
        dataMap.put("names",namesList);
        dataMap.put("countNum",countList);
        dataMap.put("eduData",list);

        //6.把map放到ServerResponse返回
        return ServerResponse.createBySuccess(dataMap);
    }
}
