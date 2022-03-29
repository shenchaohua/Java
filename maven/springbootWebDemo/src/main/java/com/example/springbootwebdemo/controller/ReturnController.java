package com.example.springbootwebdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.concurrent.TimeUnit;
import java.util.zip.Checksum;


@RestController
public class ReturnController {

    @GetMapping("/return")
    public String get() {
        System.out.println("-------------");
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        int sum=0;
        for(int i=1;i<2000000000;i++) {
            sum += i;
            sum /= i;
            sum -= i/2;
        }
        return String.valueOf(sum);
    }

    @GetMapping("/hello")
    public String get1() {
        System.out.println("-------------222");
        System.out.println();
        return new Person().toString();
    }

    class Person {
        String name;
    }
}
