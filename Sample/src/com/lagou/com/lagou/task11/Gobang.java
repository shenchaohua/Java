package com.lagou.com.lagou.task11;

import java.util.Arrays;

/*
使用双重循环实现五子棋游戏棋盘的绘制， 棋盘界面的具体效果如下
 */
public class Gobang {
    public static void main(String[] args){
        String[] charList={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
        String[][] ret = new String[charList.length+1][];

        ret[0] = new String[charList.length+1];
        ret[0][0] = " ";
        System.arraycopy(charList,0,ret[0],1,charList.length);

        for(int i=1;i<ret.length;i++){
            ret[i] = new String[ret.length];
            ret[i][0] = charList[i-1];
            for(int j=1;j<ret.length;j++){
                ret[i][j]="+";
            }
        }

        for(int x=0;x<ret.length;x++){
            for(int y=0;y<ret[x].length;y++){
                System.out.print(ret[x][y]);
            }
            System.out.println();
        }
    }
}
