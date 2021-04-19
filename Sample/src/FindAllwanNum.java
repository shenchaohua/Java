/*
编程找出 1000 以内的所有完数并打印出来。
 */
public class FindAllwanNum {
    public static void main(String[] main){
        for(int i=1;i<=1000;i++){
            int sum = 0;
            for(int j=1;j<i;j++){
                if(i%j==0){
                    sum+=j;
                }
            }
            if (sum==i){
                System.out.print(i+" ");
            }
        }
    }
}
