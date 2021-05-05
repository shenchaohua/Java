package com.czl.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 省的视图对象
 * 作用1: 查找数据库的时候,数据映射
 * 作用2: 把省份 对应的招聘人数,返回给前台  long 招聘人数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceVo {
    @JsonIgnore
    private int pid;  //省编号
    private String name; //省名称
    private long value; //该省份的招聘人数
    @JsonIgnore
    private List<CityVo> city;
}
