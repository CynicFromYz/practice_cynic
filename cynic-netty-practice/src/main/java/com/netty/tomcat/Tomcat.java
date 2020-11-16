package com.netty.tomcat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class Tomcat {


    public void start(int port) throws Exception {

        //nio
//        ServerSocketChannel server = ServerSocketChannel.open();
//        server.bind(new InetSocketAddress(8080));

        //bio
//        ServerSocket server = new ServerSocket(8080);

        //netty 主从模型
        // boss 线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // worker 线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // Netty 服务
            ServerBootstrap server = new ServerBootstrap();

            //链路式编程
            server.group(bossGroup, workerGroup)
                    //主线程处理类
                    .channel(NioServerSocketChannel.class)
                    //子线程处理类,Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {

                            //无锁化串行编程

                            //业务逻辑链路, 编码器
                            client.pipeline().addLast(new HttpResponseEncoder());
                            //解码器
                            client.pipeline().addLast(new HttpRequestDecoder());

                            //业务逻辑处理
                            client.pipeline().addLast(new TomcatHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            //sync 阻塞该线程
            ChannelFuture cf = server.bind(port).sync();
            System.out.println("tomcat 已经启动!");
            cf.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


    public static void main(String[] args) {
        try {
            new Tomcat().start(8280);
        } catch (Exception e) {

        }

    }
}
