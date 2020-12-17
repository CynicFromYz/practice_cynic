package com.netty.chat.server;

import com.netty.chat.protocol.IMDecoder;
import com.netty.chat.protocol.IMEncoder;
import com.netty.chat.server.handler.HttpHandler;
import com.netty.chat.server.handler.SocketHandler;
import com.netty.chat.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChatServer {

    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //启动引擎
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //主从模型
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            //所有自定义的业务从这里开始

                            //********支持自定义Socket协议*********//
                            sc.pipeline().addLast(new IMDecoder());
                            sc.pipeline().addLast(new IMEncoder());
                            sc.pipeline().addLast(new SocketHandler());

                            //********支持http协议*********//
                            /*解码和编码http请求*/
                            sc.pipeline().addLast(new HttpServerCodec());
                            sc.pipeline().addLast(new HttpObjectAggregator(64 * 1024));
                            //用于处理文件流的一个handler 主要用来处理大文件
                            sc.pipeline().addLast(new ChunkedWriteHandler());
                            sc.pipeline().addLast(new HttpHandler());

                            //********支持webSocket协议*********//
                            sc.pipeline().addLast(new WebSocketServerProtocolHandler("/im"));
                            sc.pipeline().addLast(new WebSocketHandler());

                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
            //sync 阻塞该线程
            ChannelFuture cf = serverBootstrap.bind(port).sync();
            System.out.println("tomcat 已经启动!");
            cf.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new ChatServer().start(8099);
    }

}
