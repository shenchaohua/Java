package com.czl.math.leetcode;

/*
给定一个仅包含数字2-9的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
输入：digits = "23"
输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
 */

import java.util.ArrayList;
import java.util.List;

public class 电话号码的全排列 {

    public static List<String> letterCombinations(String str) {
        List<String> res = new ArrayList<>();
        res.add("");

        String[] phone = new String[]{"abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

        for (int i = 0; i < str.length(); i++) {
            String s = phone[Integer.parseInt(String.valueOf(str.charAt(i)))-2];
            List<String> tmp = new ArrayList<>();
            for (int j = 0; j < s.length(); j++) {
                for (String re : res) {
                    tmp.add(re + s.charAt(j));
                }
            }
            res = tmp;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(letterCombinations("23"));
    }
}
