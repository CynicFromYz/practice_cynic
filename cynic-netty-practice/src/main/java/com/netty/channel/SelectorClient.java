package com.netty.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/22 11:28
 */
public class SelectorClient {
    public static void main(String[] args) throws IOException {
        //获取selector实例
        Selector selector = Selector.open();
        //获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //配置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //绑定连接
        serverSocketChannel.bind(new InetSocketAddress(8099));
        //将通道注册到选择器上 并制定监听事件为: [接收连接]事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        Set selectKeys = selector.selectedKeys();


    }
}
