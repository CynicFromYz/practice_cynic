package com.netty.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/30 14:19
 */
public class NettyEchoServer {

    static final int serverPort = 8099;

    ServerBootstrap serverBootstrap = new ServerBootstrap();

    public void runServer() throws InterruptedException {
        //创建反应器线程组
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup(1);
        //反应器线程组
        serverBootstrap.group(bossLoopGroup,workerLoopGroup);
        //通道类型
        serverBootstrap.channel(ServerSocketChannel.class);
        //3.设置监听端口
        serverBootstrap.localAddress(serverPort);
        //4.设置通道的参数
        serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        //5.绑定入站处理器
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(NettyServerEchoHandler.instance);
            }
        });
        //6.开始绑定服务器
        //通过调用sync同步方法阻塞直到绑定成功
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        System.out.println("服务器启动成功,监听端口:"+channelFuture.channel().localAddress());
        //7.等待通道关闭的异步任务结束
        //服务监听通道会一直等待通道关闭的异步任务结束
        ChannelFuture closeFuture = channelFuture.channel().closeFuture();
        closeFuture.sync();
        workerLoopGroup.shutdownGracefully();
        bossLoopGroup.shutdownGracefully();
    }

}
