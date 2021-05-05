package com.czl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 城市的视图对象
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityVo {
    private int cid;   //城市的id
    private String cName; //城市名称
    private long num;   //城市招聘数量
}
