/* 张书云《C语言程序设计（第2版）》p. 89 题目3. (1) */
#include <stdio.h>
int main(void)
{
    float val_purc, val_discnt;

    printf("====@=购货款计算器=@====\n输入购货面额（不带单位）：\n");
    scanf("%f", &val_purc);

    if (val_purc > 0 && val_purc <= 500) 
        {
            val_discnt = val_purc;
            printf("实际货款：%.2f 元\n", val_discnt);
        }
    else if (val_purc > 500 && val_purc <= 1000)
        {
            val_discnt = 0.9 * val_purc;
            printf("实际货款：%.2f 元\n", val_discnt);
        }
    else if (val_purc > 1000)
        {
            val_discnt = 0.8 * val_purc;
            printf("实际货款：%.2f 元\n", val_discnt);
        }
    else { printf("输入无效，请重新输入\n"); }

    return 0;
}