package com.yang.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangyang03
 * @Description 服务端：传统BIO的通信模型，接收客户端请求后创建线程处理请求
 * @create 2022-12-29 14:55
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        ServerSocket server = null;
        try {
            int port = 8080;
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket;
            while (true) {
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
            }
        }

    }

}
