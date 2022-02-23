package com.yang.step;

import org.junit.Test;

/**
 * 循环调用 的方式
 * */
public class TestStepLoop {

    public long loop(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return n;
        }

        long one = 2;//初始化为走到第二级台阶的走法
        long two = 1;//初始化为走到第一级台阶的走法
        long sum = 0;

        for (int i = 3; i <= n; i++) {
            //最后跨2步 + 最后跨1步的走法
            sum = two + one;
            two = one;
            one = sum;
        }
        return sum;
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        System.out.println(loop(50));//20365011074
        long end = System.currentTimeMillis();
        System.out.println(end - start);//<1ms
    }

}
