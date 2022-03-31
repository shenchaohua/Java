package com.czl.netty.netty.protobuf;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class NettyServerHandler2 extends SimpleChannelInboundHandler<StudentPro.MyMessage> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, StudentPro.MyMessage msg) throws Exception {
        
       if(msg.getDataType() == StudentPro.MyMessage.DataType.StudentType) {
           System.out.println("student" + msg.getStudent().getName());
       } else if (msg.getDataType() == StudentPro.MyMessage.DataType.WorkerType) {
           System.out.println("worker" + msg.getWorker().getName());
       } else {
           System.out.println("wrong type");
       }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
