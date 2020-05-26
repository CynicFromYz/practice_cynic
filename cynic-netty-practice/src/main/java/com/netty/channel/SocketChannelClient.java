package com.netty.channel;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/22 9:28
 */
public class SocketChannelClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8090));
        while (!socketChannel.finishConnect()) {
        }
        socketChannel.shutdownOutput();
        IOUtils.closeQuietly(socketChannel);

//        socketChannel.reg

//        Selector selector

        int key = SelectionKey.OP_ACCEPT|SelectionKey.OP_CONNECT;

    }
}
