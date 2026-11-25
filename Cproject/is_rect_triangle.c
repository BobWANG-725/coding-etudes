/* 张书云《C语言程序设计（第2版）》p. 89 题目3. (2) */
#include <stdio.h>
#define EPS 1e-7
int main(void)
{
    double a, b, c;

    printf("输入三角形的三边长：\n");
    scanf("%lf%lf%lf", &a, &b, &c);

    if (fabs(a * a + b * b - c * c) < EPS
        || fabs(a * a + c * c - b * b) < EPS
        || fabs(b * b + c * c - a * a) < EPS)
        {
            printf("该三角形是直角三角形\n");
        }
        else { printf("该三角形不是直角三角形\n"); }

    return 0;
}