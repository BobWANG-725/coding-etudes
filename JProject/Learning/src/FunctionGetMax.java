public class FunctionGetMax {
    public static int getMax(int x_1, int x_2) {
        if (x_1 >= x_2) return x_1;
        else return x_2;
    }

    static void main() {
        int test1 = 64;
        int test2 = 32;
        System.out.println(getMax(test1, test2));
    }
}