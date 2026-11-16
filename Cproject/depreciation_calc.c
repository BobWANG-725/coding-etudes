/*张书云《C语言程序设计（第2版）》p. 60 题目4. (2)*/
#include <stdio.h>
int main(void)
{
    double price_purchase, year_useful_life, val_salvage, val_year_depr, year_to_calc, val_book;

    printf("====@=折旧计算器=@====\n依次输入购买价格、使用年限、废品价值、要计算的年限（不带单位）：\n");
    scanf("%lf%lf%lf%lf",
           &price_purchase, &year_useful_life, &val_salvage, &year_to_calc);

    val_year_depr = (price_purchase - val_salvage) / year_useful_life;
    val_book = price_purchase - year_to_calc * val_year_depr;
    
    printf("%.1f 年折旧后价值：%.2f CNY\n", year_to_calc, val_book);

    return 0;
}