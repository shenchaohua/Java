package com.czl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndustryVo {
    private String positionName; //行业名称
    private long  num; //行业招聘数量
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date theDay;
}
