package com.yang.netty.aio;

/**
 * @author zhangyang03
 * @Description
 * @create 2022-12-30 18:29
 */
public class TimeServer {
    public static void main(String[] args) {

        int port = 8081;
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}
