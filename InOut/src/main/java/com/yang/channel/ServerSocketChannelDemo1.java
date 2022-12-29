package com.yang.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangyang03
 * @Description 测试 ServerSocketChannel的 accept方法，使用浏览器或者 telnet命令来连接
 * @create 2022-10-05 12:31
 */
public class ServerSocketChannelDemo1 {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8888;
        ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes(StandardCharsets.UTF_8));
        // 新建 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 返回 socket 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        // 设置非阻塞模式
        serverSocketChannel.configureBlocking(false);

        while (true) {
            System.out.println("Waiting for connections");
            SocketChannel accept = serverSocketChannel.accept();

            if (accept == null) {
                System.out.println("null");
                Thread.sleep(2000);
            } else {
                System.out.println("Incoming connection from: " +
                        accept.socket().getRemoteSocketAddress());
                //重复读取 buffer 数据
                buffer.rewind();
                accept.write(buffer);
                accept.close();
            }

        }
    }
}
