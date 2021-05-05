package com.czl.dao;

import com.czl.vo.CityVo;
import com.czl.vo.ProvinceVo;
import com.czl.vo.SalaryVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CountryMapDao {
    /**
     * 通过省份的城市
     * @return
     */

    List<ProvinceVo> queryProvinceCity();

    /**
     * 通过各个城市的招聘数量
     * @return
     */

    List<CityVo> queryCityNum();

    @Select("SELECT position_city NAME,AVG(m+n) AS avg_salary FROM (SELECT DISTINCT position_city,MAX(CONVERT(REPLACE(SUBSTRING_INDEX(position_salary,'-',-1),'K',''),DECIMAL(38,2))) AS m\n" +
            " ,MIN(CONVERT(SUBSTRING_INDEX(position_salary,'-',1),DECIMAL(38,2))) AS n\n" +
            " FROM `position_info_v2` WHERE position_salary NOT LIKE \"%天%\"\n" +
            "GROUP BY position_city ) a\n" +
            "GROUP BY NAME ORDER BY avg_salary DESC LIMIT 10;")
    List<SalaryVo> querySalary();
}
