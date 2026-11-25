/* 张书云《C语言程序设计（第2版）》p. 89 题目3. (4) ② */
#include <stdio.h>
int main(void)
{
    double e = 0.0;
    double denom_fac = 1.0, frac = 1.0;
    int i = 1;

    while (frac >= 1e-4)
    {
        frac = 1.0 / denom_fac;
        e += frac;

        denom_fac *= i;
        i++;
    }
    
    printf("自然常数 e 按照泰勒展开式累加至最后一项首次小于 10^{-4} 的近似值为 %.6f\n", e);

    return 0;
}