public class MoveBitTest {
    public static void main(String[] main){
        byte a = 64;
        byte b = (byte) (a << 1);
        System.out.println((byte)(a<<1));
        int i=0;
        do{
            i++;
        }while (i<1);

        switch (i){
            default:System.out.println(1);
            case 2:System.out.println(2);
            case 3:System.out.println(2);
        }
    }
}
