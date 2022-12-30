package com.yang.netty.nio;

/**
 * @author zhangyang03
 * @Description
 * @create 2022-12-29 20:12
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8081;
        new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001").start();
    }
}
