package com.lagou.math;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;

public class test {



    public static void main(String[] args) throws IOException, ParseException {
        Process proc = Runtime.getRuntime().exec("date");
        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));
        System.out.println(stdError.readLine());
        Number number = NumberFormat.getInstance().parse("120.29");
        System.out.println(number.doubleValue());
//        String.valueOf(NumberUtils.toDouble(part[fieldIndex]));
    }
}