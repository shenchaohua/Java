package com.czl.dao;

import com.czl.vo.ConverterVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EduDao {
    /**
     * 统计不同学历下的岗位
     * @return
     */
    @Select("    SELECT edu name, COUNT(*) value\n" +
            "        FROM position_info_v2\n" +
            "        WHERE edu IN ('中专/中技','初中及以下','博士','大专','本科','硕士','高中')\n" +
            "        GROUP BY edu")
    List<ConverterVo> getEdu();
}
