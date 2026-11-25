/* 张书云《C语言程序设计（第2版）》p. 60 题目4. (3) */
#include <stdio.h>
int main(void)
{
    double val_loan = 20000.00;
    double rate_year = 0.06;
    double repay_per_month = 386.66;
    double remain_prncpl_m1, remain_prncpl_m2, remain_prncpl_m3;

    //每月还款后剩余本金，教材原文称“剩余的需还款金额”，这里按照教材
    remain_prncpl_m1 = val_loan * (1.0 + rate_year / 12.0) - repay_per_month;
    remain_prncpl_m2 = remain_prncpl_m1 * (1.0 + rate_year / 12.0) - repay_per_month;
    remain_prncpl_m3 = remain_prncpl_m2 * (1.0 + rate_year / 12.0) - repay_per_month;

    printf("贷款金额：%.2f 元\n", val_loan);
    printf("年贷款利率：%.1f%%\n", 100 * rate_year);
    printf("每个月还款金额：%.2f 元\n", repay_per_month);
    printf("第一个月剩余的需还款金额：%.2f 元\n", remain_prncpl_m1);
    printf("第二个月剩余的需还款金额：%.2f 元\n", remain_prncpl_m2);
    printf("第三个月剩余的需还款金额：%.2f 元\n", remain_prncpl_m3);

    return 0;
}
