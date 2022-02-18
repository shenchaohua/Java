package com.czl.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResult<T> {
    private int code;
    private String message;
    private T Data;

    public CommonResult(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
