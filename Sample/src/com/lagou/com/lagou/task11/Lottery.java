package com.lagou.com.lagou.task11;/*
实现双色球抽奖游戏中奖号码的生成，中奖号码由 6 个红球号码和 1 个蓝球号码组成。
其中红球号码要求随机生成 6 个 1~33 之间不重复的随机号码。
其中蓝球号码要求随机生成 1 个 1~16 之间的随机号码。
 */
import javax.security.auth.callback.ConfirmationCallback;
import java.util.Random;
public class Lottery {
    public static void main(String[] main){
        Random rand = new Random();
        int[] ret = new int[7];
        for(int i=0;i<7;i++){
            if(i==6){
                ret[i] = rand.nextInt(15)+1;
            }else{
                while (true) {
                    int tmp = rand.nextInt(32) + 1;
                    boolean isRepeat = false;
                    for (int j = 0; j < i; j++) {
                        if (ret[j] == tmp) {
                            isRepeat = true;
                            break;
                        }
                    }
                    if (!isRepeat) {
                        ret[i] = tmp;
                        break;
                    }
                }
            }
        }

        for(int i=0;i<7;i++){
            System.out.print(ret[i]+" ");
        }
    }
}
