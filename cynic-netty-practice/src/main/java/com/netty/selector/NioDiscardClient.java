package com.netty.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/22 15:11
 */
public class NioDiscardClient {
    public static void startClient() throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",8099);
        //1.获取通道
        SocketChannel socketChannel = SocketChannel.open(inetSocketAddress);
        //2.切换成非阻塞模式
        socketChannel.configureBlocking(false);
        //3.不断的自旋,等待连接完成,或者做一些其他的操作
        while (!socketChannel.finishConnect()){
            //todo
            System.out.println("建立连接中......");
        }
        System.out.println("连接建立成功....");
        //4.分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello world 11".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        socketChannel.shutdownOutput();
        socketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        startClient();
    }
}
