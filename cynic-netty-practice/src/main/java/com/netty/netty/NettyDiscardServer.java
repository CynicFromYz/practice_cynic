package com.netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/27 15:59
 */
public class NettyDiscardServer {
    private final int serverPort;
    ServerBootstrap serverBootstrap = new ServerBootstrap();

    public NettyDiscardServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public void runServer() throws InterruptedException {
        //创建反应器线程组
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup stuffLoopGroup = new NioEventLoopGroup();
        //1.设置反应器线程组
        serverBootstrap.group(bossLoopGroup, stuffLoopGroup);
        //2.设置nio类型的通道
        serverBootstrap.channel(NioServerSocketChannel.class);
        //3.设置监听端口
        serverBootstrap.localAddress(serverPort);
        //4.设置通道的参数
        serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        //5.装配子通道流水线
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            //有连接到达时会创建一个通道
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //流水线管理子通道中的Handler处理器
                //向子通道流水线添加一个handler处理器
                socketChannel.pipeline().addLast(new NettyDiscardHandler());
            }
        });
        //6.开始绑定服务器
        //通过调用sync同步方法阻塞直到绑定成功
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        System.out.println("服务器启动成功,监听端口:" + channelFuture.channel().localAddress());
        //7.等待通道关闭的异步任务结束
        //服务监听通道会一直等待通道关闭的异步任务结束
        ChannelFuture closeFuture = channelFuture.channel().closeFuture();
        closeFuture.sync();
        stuffLoopGroup.shutdownGracefully();
        bossLoopGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8099;
        new NettyDiscardServer(port).runServer();
    }
}
