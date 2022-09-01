package com.czl.math.leetcode;

public class 盛最多水的容器 {

    public static int maxArea(int[] height) {

        int i = 0;
        int j = height.length-1;
        int maxArea = 0;
        while (i<j) {
            if (height[i] < height[j]) {
                maxArea = Math.max(height[i] * (j-i),maxArea);
                i ++;
            } else {
                maxArea = Math.max(height[j] * (j-i), maxArea);
                j--;
            }
        }
        return maxArea;
    }

    public static void main(String[] args) {
        System.out.println(maxArea(new int[]{1,2,3,4}));
    }
}
