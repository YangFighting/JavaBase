package com.yang.netty.frame.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhangyang03
 * @Description
 * @create 2023-01-02 15:34
 */
public class EchoServerHandler extends ChannelHandlerAdapter {
    private int counter = 0;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;

        System.out.println("This is " + ++counter + " times receive client : ["
                + body + "]");
        body +=  "$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }
}
