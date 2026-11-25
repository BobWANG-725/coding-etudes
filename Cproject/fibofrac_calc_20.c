/* 张书云《C语言程序设计（第2版）》p. 89 题目3. (3) */
#include <stdio.h>
int main(void)
{
    int fib_1 = 2, fib_2 = 1, t, i = 1;
    double frac, sum = 0.0;

    while (i <= 20)
    {
        frac = (double)fib_1 / fib_2;
        sum += frac;

        t = fib_1 + fib_2;
        fib_2 = fib_1;
        fib_1 = t;

        i++;
    }
    
    printf("分数序列 Fib_{n+1} / Fib_n（n >= 1） 的前 20 项和为 %.2f\n", sum);

    return 0;
}