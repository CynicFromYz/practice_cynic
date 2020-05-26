package com.netty.niofile;

import org.apache.log4j.net.SocketServer;
import org.springframework.expression.spel.ast.Selection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/25 9:00
 */
public class NioReceiveFileServer {
    private Charset charset = Charset.forName("UTF-8");
    static class Client{
        //文件名称
        String fileName;
        //长度
        String fileLength;
        //开始传输时间
        long startTime;
        //客户端的地址
        InetSocketAddress remoteAddress;
        //输出的文件通道
        FileChannel fileChannel;
    }
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    //使用Map保存每个文件传输,当OP_READ可读时,根据通道找到对应的对象
    static Map<SelectableChannel,Client> clientMap = new HashMap<>();
    public static void startFileReceiveServer() throws IOException {
        //1.获取选择器
        Selector selector = Selector.open();
        //2.获取通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();
        //3.设为非阻塞
        serverChannel.configureBlocking(false);
        //4.绑定连接
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",8099);
        serverChannel.bind(inetSocketAddress);
        //5.将通道注册到选择器上,并注册的IO时间为:[接收新连接]
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("server channel is listening...");
        //6.选择感兴趣的IO就绪事件(选择键集合)
        while (selector.select()>0){
            //7.获取选择键集合
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                //8.获取单个的选择键,并处理
                SelectionKey key = it.next();
                //9.判断key具体是什么事件,是否为新连接事件
                if(key.isAcceptable()){
                    //10.若接收的事件是[新连接]事件,就获取客户端连接
                    ServerSocketChannel server  = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel =server.accept();
                    if(socketChannel==null)
                        continue;
                    //11.客户端新连接,切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    //12.将客户端新连接通道注册到选择器上
                    SelectionKey selectionKey = socketChannel.register(selector,SelectionKey.OP_READ);
                    //为每一条传输通道,建立一个Client客户端对象,放入map,供后面使用
                    Client client = new Client();
                    client.remoteAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
                    clientMap.put(socketChannel,client);
                }else if(key.isReadable()){
                    //13.若接收的事件是[数据可读]事件,就读取客户端新连接

                }
                //NIO的特点只会累加,已选择的键的集合不会删除
                //如果不删除  下一次又会被select函数选中
                it.remove();
            }
        }
    }
}
