/* 课件题目 */
#include <stdio.h>
int main(void)
{
    int i = 1, sum = 0;

    while (i <= 100)
    {
        if (i % 2 == 1) { sum = sum + i; }
        i++;
    }

    printf("1~100的奇数和为%d\n", sum);

    return 0;
}