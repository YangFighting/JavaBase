package com.yang.netty.codec.marshalling;

import com.yang.netty.codec.pojo.SubscribeReq;
import com.yang.netty.codec.pojo.SubscribeResp;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Description:
 * @Author zhangyang03
 * @Date 2023/2/15 16:48
 */
public class SubReqServerHandler extends ChannelHandlerAdapter {
    private SubscribeResp resp(int subReqId) {
        SubscribeResp resp = new SubscribeResp();
        resp.setRespCode(0);
        resp.setSubReqID(subReqId);
        resp.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
        return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReq req = (SubscribeReq) msg;
        if ("Lilinfeng".equalsIgnoreCase(req.getUserName())) {
            System.out.println("Service accept client subscrib req : ["
                    + req.toString() + "]");
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }
}
