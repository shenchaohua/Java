package com.czl.netty.netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client send");
        Random random = new Random();
        StudentPro.MyMessage message = null;
        if (random.nextInt(3) == 1) {
            StudentPro.Student student = StudentPro.Student.newBuilder().setId(1).setName("czl").build();
            message = StudentPro.MyMessage.newBuilder()
                    .setDataType(StudentPro.MyMessage.DataType.StudentType)
                    .setStudent(student).build();
        } else {
            message = StudentPro.MyMessage.newBuilder()
                    .setDataType(StudentPro.MyMessage.DataType.WorkerType)
                    .setWorker(StudentPro.Worker.newBuilder().setId(1).setName("worker").build()).build();
        }
        ctx.writeAndFlush(message);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("client receive" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        cause.printStackTrace();
    }
}
