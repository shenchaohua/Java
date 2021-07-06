package com.lagou.math;

import java.util.*;

/**
 * 给一个01矩阵，1代表是陆地，0代表海洋， 如果两个1相邻，那么这两个1属于同一个岛。我们只考虑上下左右为相邻。
 * 岛屿: 相邻陆地可以组成一个岛屿（相邻:上下左右） 判断岛屿个数。
 */

public class island {
    /**
     * 判断岛屿数量
     * @param grid char字符型二维数组
     * @return int整型
     */
    public void consumeIsland(char[][]grid,Tuple<Integer,Integer> tuple){
        LinkedList<Tuple<Integer, Integer>> queue = new LinkedList<>();
        queue.add(tuple);
        while(queue.size()!=0){
            Tuple<Integer, Integer> point = queue.removeLast();
            int i = point.x;
            int j = point.y;
            if(j-1>=0 && grid[i][j-1]==1){
                queue.add(new Tuple<>(i, j - 1));
            }
            if(j+1<grid[0].length && grid[i][j+1]==1){
                queue.add(new Tuple<>(i,j+1));
            }
            if(i-1>=0 && grid[i-1][j]==1){
                queue.add(new Tuple<>(i-1,j));
            }
            if(i+1<grid.length && grid[i+1][j]==1){
                queue.add(new Tuple<>(i+1,j));
            }
            grid[i][j]=0;
        }
    }
    public int solve (char[][] grid) {
        int i = grid.length;
        int j = grid.length;
        int cnt =0;
        for(int a=0;a<i;a++){
            for(int b=0;b<j;b++){
                if(grid[a][b]==1){
                    consumeIsland(grid,new Tuple<Integer,Integer>(a,b));
                    cnt++;
                    a = 0;
                    b = 0;
                }
            }
        }
        return cnt;
    }
     public static void main(String[] args){
         char[][] data = {{1,1,0,0,0},{0,1,0,1,1},{0,0,0,1,1},{0,0,0,0,0},{0,0,1,1,1}};
         int cnt = new island().solve(data);
         System.out.println(cnt);
     }
}
class Tuple<X, Y> {
    public final X x;
    public final Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}