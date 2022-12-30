package com.yang.netty.pio;

import com.yang.netty.bio.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangyang03
 * @Description 伪异步IO，通过线程池来处理多个客户端请求
 * @create 2022-12-29 18:19
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {

        int port = 8081;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 1000);
            while (true) {
                Socket socket = server.accept();
                // 将请求的socket 封装成 线程池的任务
                singleExecutor.execute(new TimeServerHandler(socket));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
                server = null;

            }
        }

    }
}
