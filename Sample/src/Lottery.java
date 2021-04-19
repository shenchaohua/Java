/*
实现双色球抽奖游戏中奖号码的生成，中奖号码由 6 个红球号码和 1 个蓝球号码组成。
其中红球号码要求随机生成 6 个 1~33 之间不重复的随机号码。
其中蓝球号码要求随机生成 1 个 1~16 之间的随机号码。
 */
import java.util.Random;
public class Lottery {
    public static void main(String[] main){
        Random rand = new Random();
        for(int i=0;i<7;i++){
            if(i==6){
                System.out.print(rand.nextInt(15)+1+" ");
            }else{
                System.out.print(rand.nextInt(32)+1+" ");
            }
        }
    }
}
