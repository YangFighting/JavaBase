package com.yang.netty.nio;

/**
 * @author zhangyang03
 * @Description
 * @create 2022-12-29 19:56
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8081;
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        System.out.println("The time server is start in port : " + port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}
