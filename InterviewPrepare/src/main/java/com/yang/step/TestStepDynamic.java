package com.yang.step;

import org.junit.Test;

/**
 * 动态规划 的方式
 */
public class TestStepDynamic {
    // 用数据记录计算过的值
    public static long[] A = new long[1000];

    public static long f(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1 || n == 2) {
            A[n] = n;
            return n;
        }
        if (A[n] > 0) {
            // 数组没有初始化
            return A[n];
        } else {
            A[n] = f(n - 1) + f(n - 2);
            return A[n];
        }
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        System.out.println(f(50)); // 20365011074
        long end = System.currentTimeMillis();
        System.out.println(end - start);//<1ms
    }

}
