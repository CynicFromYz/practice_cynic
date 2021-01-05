package com.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    private int port;
    private InetSocketAddress inetSocketAddress;

    private Selector selector;

    public NioServer(int port) {
        try {
            this.port = port;
            inetSocketAddress = new InetSocketAddress(this.port);
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(inetSocketAddress);
            //默认为阻塞,手动设置为非阻塞
            serverChannel.configureBlocking(false);
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("服务器准备就绪,监听端口:" + this.port);

        } catch (Exception e) {
            System.out.println("error...");
        }
    }

    public void listen() {
        try {
            while (true) {
                //有多少人在服务大厅排队
                int wait = this.selector.select();
                if (wait == 0) {
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    process(selectionKey);
                    iterator.remove();
                }
            }
        } catch (Exception e) {
        }
    }

    private void process(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        if (key.isAcceptable()) {
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel client = channel.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            int length = client.read(byteBuffer);
            if (length > 0) {
                byteBuffer.flip();
                String content = new String(byteBuffer.array(), 0, length);
                System.out.println(content);
                client.configureBlocking(false);
                client.register(selector, SelectionKey.OP_WRITE);
            }
            byteBuffer.clear();
        } else if (key.isWritable()) {
            SocketChannel client = (SocketChannel) key.channel();
            byteBuffer = byteBuffer.wrap("hello world".getBytes());
            client.write(byteBuffer);
        }
    }

    public static void main(String[] args) {
        new NioServer(8080).listen();
    }
}
