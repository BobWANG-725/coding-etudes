public class CounterEvenNumber {
    public static int countEven(int[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 == 0) {
                count ++;
            }
        }
        return count;
    }

    static void main() {
        int[] arr = {1, 2, 3, 4, 5, 6};
        System.out.println("数组arr中偶数的个数为" + countEven(arr));
    }
}