package com.czl.dao;

import com.czl.vo.ConverterVo;
import com.czl.vo.IndustryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IdustryDao {
    /**
     * 统计各个城市招聘情况
     * @return
     */
    @Select("       SELECT position_city as name ,COUNT(*) as value FROM position_info_v2\n" +
            "        WHERE position_city != ''\n" +
            "        GROUP BY position_city  ORDER BY value DESC\n" +
            "        LIMIT 10")
    public List<ConverterVo> countCityPosition() ;
    @Select("        SELECT DISTINCT position_type_name  positionName, SUM(position_num)  AS num\n" +
            "        FROM position_type_info_v2\n" +
            "        GROUP BY position_type_name ORDER BY num DESC LIMIT 10;")
    List<IndustryVo> countIndustryTop();

    /**
     * 统计热门行业对比
     * @param industryName
     * @param startDate
     * @param endDate
     * @return
     */
    @Select("        SELECT position_type_name  positionName, SUM(position_num) num , DATE_FORMAT(update_time,'%Y-%m-%d') AS theDay\n" +
            "        FROM position_type_info_v2\n" +
            "        WHERE position_type_name = #{industryName}\n" +
            "        AND update_time <= #{startDate}\n" +
            "        AND update_time >= #{endDate}\n" +
            "        GROUP BY theDay")
    List<IndustryVo> getIndustryData(@Param("industryName") String industryName,
                                     @Param("startDate") String startDate,
                                     @Param("endDate") String endDate);
}
