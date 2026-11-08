import java.util.Locale;

/**
 * 格式化输出（Formatted Output）速查与陷阱演示。
 *
 * 用法：直接运行 main，对照每段标题看输出。
 * 格式说明符的完整语法：
 *
 *     %[argument_index$][flags][width][.precision]conversion
 *
 *     %   -  10   .2   f
 *     |   |    |    |   └─ conversion（类型：d/f/s/c/x/e/b/n/%）
 *     |   |    |    └───── precision（小数位数 / 字符串最大长度）
 *     |   |    └────────── width（最小宽度，不够就补空格）
 *     |   └─────────────── flags（- 左对齐，0 补零，+ 带符号，, 千分位，( 括号负数）
 *     └─────────────────── argument_index（1$ 表示用第 1 个参数）
 *
 * 这是一份可以直接改数字、看输出的实验代码。
 */
public class FormatDemo {

    public static void main(String[] args) {
        section("1. print / println / printf：谁负责换行");
        System.out.print("print 不换行 → ");
        System.out.println("println 换行");
        System.out.printf("printf 也不换行");
        System.out.printf("（所以要自己写 %%n）%n");
        System.out.println("↑ 上一行末尾的 %n 才是换行");
        System.out.println("注意：printf 里写 %% 才会输出一个百分号，因为 % 是格式引导符");

        section("2. 整数的 flags");
        System.out.printf("默认   : %d%n", 42);
        System.out.printf("宽度 5 : [%5d]%n", 42);
        System.out.printf("左对齐 : [%-5d]%n", 42);
        System.out.printf("补零   : [%05d]%n", 42);
        System.out.printf("带符号 : [%+d] [%+d]%n", 42, -42);
        System.out.printf("千分位 : %,d%n", 1234567890);
        System.out.printf("十六进制: %x  八进制: %o%n", 255, 255);
        System.out.printf("负数括号: %(d  %(d%n", 42, -42);

        section("3. 浮点数的 flags 与精度");
        System.out.printf("默认     : %f%n", 3.14159265358979);
        System.out.printf("两位小数 : %.2f%n", 3.14159265358979);
        System.out.printf("宽度 10  : [%10.3f]%n", 3.14159265358979);
        System.out.printf("左对齐   : [%-10.3f]%n", 3.14159265358979);
        System.out.printf("科学计数 : %e%n", 31415.9);
        System.out.printf("自动选择 : %g  %g%n", 0.0000314, 31415926.5);
        System.out.printf("千分位   : %,.2f%n", 1234567.891);

        section("4. 字符串：width 是补齐，precision 是截断");
        System.out.printf("[%s]%n", "Java");
        System.out.printf("[%10s]%n", "Java");
        System.out.printf("[%-10s]%n", "Java");
        System.out.printf("[%.3s]%n", "Java");       // 精度对字符串是"最多取几个字符"

        section("5. 参数索引：同一参数可以用多次，也可打乱顺序");
        System.out.printf("%2$s 今年 %1$d 岁%n", 18, "Bob");
        System.out.printf("%1$s / %1$s / %2$d%n", "同一个参数", 99);

        section("6. 陷阱一：%b 的语义不是\"真假\"，而是\"非 null\"");
        System.out.printf("%b  %b  %b  %b%n", "随便一个字符串", null, false, 0);

        section("7. 陷阱二：%s 会自动调用 toString()");
        Object o = new Object();
        System.out.printf("[%s]%n", o);
        System.out.println("↑ 没重写 toString 的类，打印出来只有类名@哈希码");

        section("8. 舍入：%.1f 到底怎么进位");
        System.out.printf("0.25  → %.1f%n", 0.25);
        System.out.printf("0.35  → %.1f%n", 0.35);
        System.out.printf("2.675 → %.2f%n", 2.675);
        System.out.printf("2.5   → %.0f%n", 2.5);
        System.out.println("↑ Java 先取 double 的最短十进制表示（\"0.35\"），再四舍五入");
        System.out.println("  但 2.675 在二进制里其实是：");
        System.out.println("  " + new java.math.BigDecimal(2.675));
        System.out.println("  C 的 printf 按这个精确值算，%.2f 会得到 2.67；Java 给 2.68");

        section("9. Locale 会改变分隔符");
        System.out.printf(Locale.US,      "US      : %,.2f%n", 1234567.891);
        System.out.printf(Locale.GERMANY, "GERMANY : %,.2f%n", 1234567.891);
        System.out.printf(Locale.CHINA,   "CHINA   : %,.2f%n", 1234567.891);

        section("10. String.format 与 .formatted()：拿到字符串再处理");
        String line = String.format("%-8s|%6.2f|%3d%n", "张三", 96000.0, 28);
        System.out.print(line);
        System.out.print("x=%d, y=%.3f".formatted(7, Math.PI) + "\n");

        section("11. 文本块（Java 15+）适合拼表格/JSON");
        String table = """
                %-8s %-10s %s
                %-8s %-10s %s
                """.formatted("Name", "Type", "Annual",
                              "Zhang", "Monthly", "96000.00");
        System.out.print(table);
        System.out.println("↑ 换成中文名字会歪：中文字符在终端里占两个字符宽，"
                + "而 %s 的 width 按字符个数算，不按显示宽度算");

        section("12. 参数个数不对会怎样");
        try {
            // 先用 String.format 拼好再输出：异常发生时还没有写出任何字符
            String s = String.format("%d %d", 1);      // 少给参数
        } catch (Exception e) {
            System.out.println("少参数 → " + e.getClass().getSimpleName());
        }
        System.out.printf("%d%n", 1, 2, 3);            // 多给参数：不报错，直接忽略
        System.out.println("↑ 多给参数不报错，白白丢掉了 2 和 3");
        System.out.println("↑ 这也说明：先 String.format 再输出，比直接 printf 更好排查");
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("========== " + title + " ==========");
    }
}
