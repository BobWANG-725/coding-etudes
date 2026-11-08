import java.lang.StringBuilder;


public class Array2DSumEtMax {
    public static int arraySum (int[][] arr2D) {    //求和
        int sum = 0;
        for (int i = 0; i < arr2D.length; i++) {
            for (int j = 0; j < arr2D[i].length; j++) {
                sum += arr2D[i][j];
            }
        }
        return sum;
    }

    public static int arrayMax (int[][] arr2D) {    //求最大值
        int max = arr2D[0][0];
        for (int i = 0; i < arr2D.length; i++) {
            for (int j = 0; j < arr2D[i].length; j++) {
                if (max < arr2D[i][j]) max = arr2D[i][j];
            }
        }
        return max;
    }

    public static StringBuilder arrayMaxCoordination (int[][] arr2D) {    //最大值坐标，格式为（行，列）
        int max = arrayMax(arr2D);
        StringBuilder coord = new StringBuilder();
        for (int i = 0; i < arr2D.length; i++) {
            for (int j = 0; j < arr2D[i].length; j++) {
                if (arr2D[i][j] == max) {
                    if (coord.length() > 0) coord.append("、");
                    coord.append("（") .append(i + 1) .append("，") .append(j + 1) .append("）");    //(i + 1)、(j + 1)分别为行数和列数，非索引
                }
            }
        }
        coord.append("。");
        return coord;
    }

    static void main () {
        int[][] data = {
                {3, 5, 1},
                {9, 9},
                {4, 8, 6, 7}
        };
        System.out.println("数组data的和为" + arraySum(data));
        System.out.println("最大值为" + arrayMax(data) + "，位于" + arrayMaxCoordination(data));
    }
}