import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

/*
自定义数组扩容规则，当已存储元素数量达到总容量的 80%时，扩容 1.5 倍。
例如，总容量是 10，当输入第 8 个元素时，数组进行扩容，容量从 10 变 15。
 */

public class ArrayResize {
    public static void main(String[] args){
        System.out.println("请输入初始化数组的长度：");
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        int[] storage = new int[num];
        int index=0;
        while (true){
            System.out.println("是否继续输入数字？输入Y继续");
            String ack = scanner.next();
            if(!ack.equals("Y")){
                System.out.println("退出中。。。");
                break;
            }
            System.out.println("输入下一个要存储的数字");
            int n = scanner.nextInt();
            storage[index]=n;
            index++;
            if(index>=storage.length*0.8){
                System.out.println("触发扩容。。。");
                System.out.println("扩容前数组的容量为："+storage.length);
                int[] new_storage = new int[storage.length*15/10];
                System.arraycopy(storage,0,new_storage,0,storage.length);
                storage = new_storage;
                System.out.println("扩容后容量为："+storage.length);
            }
        }
    }
}
