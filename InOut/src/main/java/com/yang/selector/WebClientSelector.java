package com.yang.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author zhangyang03
 * @Description
 * @create 2022-10-04 21:00
 */
public class WebClientSelector {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8090));
        ByteBuffer writeBuffer = ByteBuffer.allocate(32);
        ByteBuffer readBuffer = ByteBuffer.allocate(32);



        while (true) {
            String dataStr = "hello " + System.currentTimeMillis();
            // 客户端写入数据到 buffer
            writeBuffer.clear();
            writeBuffer.put(dataStr.getBytes());
            // 读取 buffer 的数据，写入到 channel
            writeBuffer.flip();
            socketChannel.write(writeBuffer);

            //读取channel返回的数据，写入到 buffer 中
            readBuffer.clear();
            socketChannel.read(readBuffer);
            // 读取 buffer 中返回的数据
            readBuffer.flip();
            System.out.println("server: " + new String(readBuffer.array()));
        }
    }
}
