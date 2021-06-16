package com.lagou.math;

public class beibao {
    public static void main(String[] args) {
        int[] values = {6, 3, 5, 4, 6};
        int[] weights = {2, 2, 6, 5, 4};
        int capacity = 10;
        System.out.println(getMaxVal(values, weights, capacity));
    }
    static int getMaxVal(int[] values, int[] weights, int capacity) {
//过滤掉不合理的值
        if (values == null || values.length == 0) {
            return 0;
        }
        if (weights == null || weights.length == 0) {
            return 0;
        }
        if (capacity < 0) {
            return 0;
        }
//使用递推方式:dp(i,j):最大承重为j,有前i件物品可选时的最大总价值
        int[][] dp = new int[values.length + 1][capacity + 1]; //数组初始化默认值就是0
//遍历
        for (int i = 1; i <= values.length; i++) {
            for (int j = 1; j <= capacity; j++) {
//翻译状态转移方程
                if (j < weights[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weights[i - 1]] +
                            values[i - 1]);
                }
            }
        }
        return dp[values.length][capacity];
    }
}