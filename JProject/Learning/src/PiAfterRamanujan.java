import java.util.Scanner;


public class PiAfterRamanujan {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Enter n: ");
            if (!input.hasNextInt()) {
                System.out.print("ERROR: n should be an integer");
                return;
            }
            int n = input.nextInt();
            if (n < 1) {
                System.out.print("ERROR: n should greater than or equal to 1");
                return;
            }
            System.out.println("CALCULATION COMPLETED: " + piRamanujan(n));
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static double piRamanujan(int n) {
        final double factorConst = 2d * Math.sqrt(2d) / 9801d;
        final double pow_396_4 = Math.pow(396d, 4d);

        double afterSigma = 0d;
        double a = 1103d; // a_0

        for (int k = 0; k < n; k++) {
            afterSigma += a;
            double numerator = (4d * k + 1) * (4d * k + 2) * (4d * k + 3) * (4d * k + 4);
            double denominator = Math.pow(k + 1, 4) * pow_396_4;
            double factor = (1103d + 26390d * (k + 1)) / (1103d + 26390d * k);
            a *= numerator / denominator * factor;
        }

        return 1d / (afterSigma * factorConst);
    }
}
