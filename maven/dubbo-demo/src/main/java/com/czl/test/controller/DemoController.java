package com.czl.test.controller;


import com.chargeProject.interfaces.interfaceX.ICommunityCorpRelService;
import com.czl.test.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    public DemoService demoService;

    @GetMapping("/hello")
    public String get() {
        return "hello";
    }

    @GetMapping("/test")
    public String test() {
        // 根据小区id和货币类型查询对应的法人账号和编号
        return demoService.test("s","s1", "s2").toString();
//        return communityCorpRelImpl.RelExist("s", "s1", "s2").toString();
    }
}
