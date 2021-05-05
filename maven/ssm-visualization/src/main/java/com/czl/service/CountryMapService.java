package com.czl.service;

import com.czl.common.ServerResponse;
import com.czl.vo.ProvinceVo;
import com.czl.vo.SalaryVo;

import java.util.List;
import java.util.Map;

public interface CountryMapService {
    /**
     * 统计各个省份的招聘人数
     * @return
     */
    ServerResponse<List<ProvinceVo>> getMapData();

    ServerResponse<Map<String,Object>> getSalaryData();
}
