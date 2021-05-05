package com.czl.service;

import com.czl.common.ServerResponse;

import java.util.Map;

public interface EduService {
    /**
     * 统计不同学历下的岗位数量
     * @return
     */
    ServerResponse<Map<String, Object>> getEduData();
}
