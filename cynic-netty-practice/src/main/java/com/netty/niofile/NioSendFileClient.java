package com.netty.niofile;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/25 8:43
 */
public class NioSendFileClient {
    private Charset charset = Charset.forName("UTF-8");

    /**
     * 向服务器端发送文件
     */
    public static void sendFile() throws IOException {
        String srcPath = "E:\\test\\filesNames.txt";
        System.out.println("发送文件路径" + srcPath);
        String destFile = "";
        File file = new File(srcPath);
        FileOutputStream fos = new FileOutputStream(file);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        FileChannel fileChannel = fos.getChannel();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8099);
        SocketChannel socketChannel = SocketChannel.open(inetSocketAddress);
        socketChannel.configureBlocking(false);
        while (!socketChannel.finishConnect()) {
            System.out.println("建立连接中.....");
        }
        while (fileChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        fos.close();
        fileChannel.close();
        socketChannel.shutdownOutput();
        socketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        sendFile();
    }
}
