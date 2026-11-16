#include <stdio.h>

/* 用来验证环境是否配好的最小程序。
   编译通过 + 打印出下面两行，则 gcc 和 VS Code 都通了。 */

int main(void)
{
    int a = 7;
    int b = 2;

    printf("hello, C\n");
    printf("a / b = %d,  (double)a / b = %.2f\n", a / b, (double)a / b);

    return 0;
}
