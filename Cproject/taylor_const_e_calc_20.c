#include <stdio.h>
int main(void)
{
    double e = 1.0;
    double denom_fac = 1.0, frac = 1.0;
    int i = 0;

    while (frac >= 1e-4)
    {
        i++;
        denom_fac *= i;

        frac = 1.0 / denom_fac;
        e += frac;
    }
    
    printf("自然常数 e 按照泰勒展开式累加至最后一项首次小于 10^{-4} 的近似值为 %.6f\n", e);

    return 0;
}