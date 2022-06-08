package com.czl.math.leetcode;

/*
输入：s = "babad"
输出："bab"
解释："aba" 同样是符合题意的答案。
*/

public class 最长回文子串 {

    public static String findMaxString(String str) {
        int length = str.length();
        boolean[][] dp = new boolean[length][length];
        int maxLen =  1;
        int start = 0;

        for (int i = 1; i < length; i++) {
            for (int j = 0; j < i; j++) {
                if (str.charAt(i) == str.charAt(j) && (i-j <=2 || dp[i-1][j+1])) {
                    dp[i][j] = true;
                    if (maxLen < i - j + 1) {
                        maxLen = i-j+1;
                        start = j;
                    }
                }
            }
        }
        return str.substring(start, start+maxLen);
    }

    public static void main(String[] args) {
        System.out.println(findMaxString("baab"));
    }
}
