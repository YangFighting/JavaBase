package com.yang.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhangyang03
 * @Description
 * @create 2022-10-04 20:37
 */
public class WebServerSelector {
    public static void main(String[] args) throws IOException {
        // 创建 Selector 选择器
        Selector selector = Selector.open();

        // 创建 ServerSocketChannel 通道，并绑定监听端口
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress("localhost", 8090));

        // 设置 Channel 通道是非阻塞模式
        ssc.configureBlocking(false);

        ByteBuffer readBuff = ByteBuffer.allocate(1024);
        ByteBuffer writeBuff = ByteBuffer.allocate(128);

        // 把 Channel 注册到 Socketor 选择器上，监听连接事件
        int validOps = ssc.validOps();
        ssc.register(selector, validOps);

        while (true) {
            // 监测通道的就绪状况
            // 为什么 nReady 始终为 1 , 不应该是 客户端，服务端两个socket吗
            // 因为select查询出已经就绪的通道操作, 服务端监听的通道操作是接收，
            // 第一次进来的时候是服务端的socket，之后把客户端socket通道注册到selector
            // 之后处理的是客户端的socket
            int nReady = selector.select();
            System.out.println("nReady: " + nReady);
            // 获取就绪 channel 集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            // 遍历就绪 channel 集合
            while (iterator.hasNext()) {
                SelectionKey nextKey = iterator.next();
                iterator.remove();
                if (nextKey.isAcceptable()) {
                    SocketChannel socketChannelClient = ssc.accept();
                    socketChannelClient.configureBlocking(false);
                    // 将接收的socket注册到selector中，通道操作 改为 可读（而不是增加）
                    socketChannelClient.register(selector, SelectionKey.OP_READ);
                    System.out.println("The new connection is accepted from the client: " + socketChannelClient);

                } else if (nextKey.isReadable()) {
                    SocketChannel socketChannelClient = (SocketChannel) nextKey.channel();
                    // 捕获异常，客户端在断开连接时服务端继续运行
                    try {
                        readBuff.clear();
                        socketChannelClient.read(readBuff);
                        readBuff.flip();
                        System.out.println("Message read from client: " + new String(readBuff.array()));

                        // 将可读socket的通道操作改为 可写
                        nextKey.interestOps(SelectionKey.OP_WRITE);
                    } catch (Exception e) {
                        socketChannelClient.close();
                        System.out.println("Client disconnected");
                    }

                } else if (nextKey.isWritable()) {
                    // 将 接收响应写到 buffer中
                    writeBuff.clear();
                    writeBuff.put(("received " + System.currentTimeMillis()).getBytes());

                    writeBuff.flip();
                    SocketChannel socketChannel = (SocketChannel) nextKey.channel();
                    // 将服务端的接收响应返回给客户端
                    System.out.println("Message write from the server: " + new String(writeBuff.array()));
                    socketChannel.write(writeBuff);

                    //  将可写 socket的通道操作改为可读
                    nextKey.interestOps(SelectionKey.OP_READ);
                }
            }


        }


    }
}
