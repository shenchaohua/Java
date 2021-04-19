import java.util.Scanner;

public class ArithmetricTimeTest {
    public static void main(String[] args){
        System.out.println("请输入秒数");
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        int hour = num / 3600;
        int minutes = num % 3600 / 60;
        int second = num % 60;
        System.out.println(hour + " " + minutes + " " + second);
    }
}
