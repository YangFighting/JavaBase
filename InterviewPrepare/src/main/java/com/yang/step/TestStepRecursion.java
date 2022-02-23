package com.yang.step;

import org.junit.Test;

/**
 * 实现f(n)：求n步台阶，一共有几种走法
 * 递归
 */
public class TestStepRecursion {
    public long f(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return n;
        }
        return f(n - 2) + f(n - 1);
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        System.out.println(f(50));//20365011074
        long end = System.currentTimeMillis();
        System.out.println(end - start);//49590 ms
    }
}
