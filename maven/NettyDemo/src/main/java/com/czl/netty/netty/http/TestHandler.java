package com.czl.netty.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class TestHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {

        // http请求
        if (httpObject instanceof HttpRequest) {
            System.out.println(httpObject.getClass());
            System.out.println(ctx.channel().remoteAddress());

            HttpRequest request = (HttpRequest) httpObject;
            URI uri = new URI(request.uri());
            if (uri.getPath().equals("/favicon.ico")) {
                return;
            }

            ByteBuf byteBuf = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            ctx.writeAndFlush(response);
        }
    }
}
