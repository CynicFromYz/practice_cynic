package com.netty.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/22 14:05
 */
public class NioDiscardServer {
    public static void startDiscardServer() throws IOException {
        //1.获取选择器
        Selector selector = Selector.open();
        //2.获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //3.设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //4.绑定连接
        serverSocketChannel.bind(new InetSocketAddress(8099));
        System.out.println("服务器启动.......");
        //5.将通道注册的[接收新连接]IO事件注册到选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6.轮询感兴趣的IO就绪时间(选择键集合)
        while (selector.select() > 0) {
            //7.获取选择键集合
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeySet.iterator();
            while (keyIterator.hasNext()) {
                //8.获取单个的键,并处理
                SelectionKey selectionKey = keyIterator.next();
                //9.判断key的具体事件
                if (selectionKey.isAcceptable()) {
                    //10.若选择的键的IO事件是[连接就绪]事件,就获取客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //11.切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    //12.将该新连接的通道注册到选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    //13.若选择键的IO事件是[可读]事件,读取数据
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //14.读取数据  然后丢弃
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int length = 0;
                    while ((length = socketChannel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(), 0, length));
                        byteBuffer.clear();
                    }
                    socketChannel.close();
                }
                //15.移除选择键
                keyIterator.remove();
            }
        }
        //16.关闭连接
        serverSocketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        startDiscardServer();
    }
}
