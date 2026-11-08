import java.util.Arrays;


public class ArrayReverse {
    public static void reverse(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    static void main() {
        int[] arrTest = {1, 2, 3, 4, 5};
        reverse(arrTest);
        System.out.println(Arrays.toString(arrTest));
    }
}