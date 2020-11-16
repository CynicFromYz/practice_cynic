package com.netty.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class AioServer {
    private int port;

    public AioServer(int port) {
        this.port = port;
    }

    public void listen() {
        try {
            AsynchronousServerSocketChannel sever = AsynchronousServerSocketChannel.open();
            sever.bind(new InetSocketAddress(this.port));

            sever.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

                @Override
                public void completed(AsynchronousSocketChannel client, Object attachment) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    try {
                        len = client.read(byteBuffer).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    byteBuffer.flip();
                    if (len > 0) {
                        System.out.println(new String(byteBuffer.array()));
                    }
                    byteBuffer.clear();
                }

                @Override
                public void failed(Throwable exc, Object attachment) {

                }
            });
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {

        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new AioServer(8080).listen();
    }
}
