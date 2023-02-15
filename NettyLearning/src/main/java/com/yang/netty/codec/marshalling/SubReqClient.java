package com.yang.netty.codec.marshalling;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Description: 9.3 Marshalling 客户端开发
 * @Author zhangyang03
 * @Date 2023/2/15 17:41
 */
public class SubReqClient {
    public static void main(String[] args) throws Exception {
        int port = 8081;
        new SubReqClient().connect(port, "127.0.0.1");
    }

    private void connect(int port, String host) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            socketChannel.pipeline().addLast(new SubReqClientHandler());
                        }
                    });
            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();

        }

    }
}
