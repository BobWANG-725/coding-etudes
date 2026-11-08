public class CheckerPositive {
    public static boolean isPositive(int param) {
        return param > 0;    //在这里，param > 0即是一个布尔变量
    }

    static void main() {
        System.out.println(isPositive(-5));
        System.out.println(isPositive(10));
    }
}