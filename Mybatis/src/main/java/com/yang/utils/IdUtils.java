package com.yang.utils;

import java.util.Random;

/**
 * @author zhangyang
 * @date 2021/12/12 22:31
 **/
public class IdUtils {

    private IdUtils() {
    }

    public static long genItemId() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //加上两位随机数
        Random random = new Random();
        int end2 = random.nextInt(99);
        //如果不足两位前面补0
        String str = millis + String.format("%02d", end2);
        return new Long(str);
    }
}
