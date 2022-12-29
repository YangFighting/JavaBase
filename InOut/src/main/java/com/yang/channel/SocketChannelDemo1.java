package com.yang.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author zhangyang03
 * @Description 测试 socketChannel的 read方法
 * @create 2022-10-05 13:22
 */
public class SocketChannelDemo1 {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
        // tcp客户端一般不设置 非阻塞模式
//        socketChannel.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            System.out.println("data: " + (char) byteBuffer.get());
        }
        socketChannel.close();
        System.out.println("read over");
    }
}
