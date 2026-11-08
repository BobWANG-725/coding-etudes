/// @author = Bob Wang, Peaksol
import java.util.Scanner;


public class BisectionMethodRevised {
    public static double f(double x) {
        return 4 * Math.pow(x, 4) + 25 * Math.pow(x, 3) + 2 * Math.pow(x, 2) + 8 * x + 1;
    }

    public static void main(String[] args) {
        double eps, a, b, c, f_c;
        Scanner s = new Scanner(System.in);
        System.out.print("二分法求函数零点\n" + "请输入初始区间左端点：");
        a = s.nextDouble();
        System.out.print("请输入初始区间右端点：");
        b = s.nextDouble();
        System.out.print("请输入精确度 ε：");
        eps = s.nextDouble();
        s.close();
        do {
            c = (a + b) / 2;
            f_c = f(c);
            if (f(a) * f_c < 0) b = c;
            else a = c;
        }
        while (Math.abs(a - b) >= eps && f_c != 0);
        System.out.println("f(x) 的零点精确到 ε = " + eps + " 时的近似值为 x₀ = " + a);
    }
}
