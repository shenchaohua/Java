package com.czl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转换对象:  把数据库中封装好的数据, 转换成  name:xx   value:xxx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConverterVo {
    private String name;
    private String value;
}
