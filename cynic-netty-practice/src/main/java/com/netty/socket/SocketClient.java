package com.netty.socket;

import org.apache.tomcat.util.net.NioChannel;
import org.apache.tomcat.util.net.SocketBufferHandler;
import sun.nio.ch.FileChannelImpl;

import java.nio.IntBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/18 20:02
 */
public class SocketClient {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        System.out.println(intBuffer.position());
        System.out.println(intBuffer.capacity());
        System.out.println(intBuffer.limit());
        intBuffer.put(1);
        intBuffer.put(2);
        System.out.println(intBuffer.position());
        intBuffer.flip();
        intBuffer.mark();
        intBuffer.put(3);
        intBuffer.reset();
//        intBuffer.
        intBuffer.compact();
        intBuffer.get();
        Channel channel = new NioChannel(new SocketBufferHandler(5,5,true));

    }
}
