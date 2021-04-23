package com.lagou.task12;

public class StringBuilderTEst {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("sampl");
        System.out.println("" + sb.capacity() + " " + sb.length());
        sb.append("sssssssssssssssssssssssssssssssssssssss");
        System.out.println("" + sb.capacity() + " " + sb.length());
        System.out.println(46>>1);


        StringBuilder sb2 = new StringBuilder("sampl2");
        StringBuilder sb3 = new StringBuilder("sampl3");
        sb2.append(sb3);
        System.out.println(sb2);
    }
}
