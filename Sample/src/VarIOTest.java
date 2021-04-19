import java.util.Scanner;

public class VarIOTest {
    public static void main(String[] args){
        String name;
        int age;
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        name = scanner.next();
        age = scanner.nextInt();
        System.out.println(name + age);
    }
}
