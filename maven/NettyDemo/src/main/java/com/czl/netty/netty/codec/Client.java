package com.czl.netty.netty.codec;

import com.czl.netty.netty.groupChat.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

public class Client {

    public Client() {

    }

    public void run() throws InterruptedException {

        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new LongToByteDecoder());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
//                Long line = scanner.nextLong();
//                System.out.println(line);
//                channelFuture.channel().writeAndFlush(line);
                String s = scanner.nextLine();
                System.out.println(s);
                channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(s, CharsetUtil.UTF_8));
            }
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Client().run();
    }

}
