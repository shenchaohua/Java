package com.lagou.com.lagou.task11;/*
提示用户输入年月日信息，判断这一天是这一年中的第几天并打印。
 */
import java.util.Scanner;
public class DayOfYear {
    public static void main(String[] main){
        System.out.println("请分别输入年份 月份 日数");
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        int month = scanner.nextInt();
        int day = scanner.nextInt();

        int[] daysOfMonth = {31,28,31,30,31,30,31,31,30,31,30,31};

        if(( year%100==0 && year % 400==0) || (year % 100 !=0 && year % 4 == 0)){
            daysOfMonth[1]=29;
        }
        int DayOfTheYear = day;
        for(int i=0;i<month-1;i++){
            DayOfTheYear += daysOfMonth[i];
        }

        System.out.println("该输入的时间是"+year+"年的第"+DayOfTheYear+"天");
    }
}