package com.lagou.math;

import com.alibaba.druid.sql.visitor.functions.Char;

import java.util.Arrays;

public class ReverseString {

    public static boolean isStringReverse(String s1,String s2){
        if (s1.length()!=s2.length() || s1.length()==1 ){
            return false;
        }
        char[] a = s1.toCharArray();
        char[] b = s2.toCharArray();
        for(int i = 0;i<b.length;i++){
            if(i+1<b.length && b[i]==a[b.length-1] && b[i+1]==a[0]){
                return Arrays.equals(Arrays.copyOfRange(b,i+1,b.length),Arrays.copyOfRange(a,0,b.length-i-1)) &&
                        Arrays.equals(Arrays.copyOfRange(b,0,i+1),Arrays.copyOfRange(a,b.length-i-1,b.length));
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String s1 = "aba";
        String s2 = "baa";
        System.out.println(ReverseString.isStringReverse(s1,s2));
    }
}
