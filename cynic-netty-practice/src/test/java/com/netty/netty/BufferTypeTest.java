package com.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/30 10:22
 */
public class BufferTypeTest {
    final static Charset UTF_8 = Charset.forName("UTF-8");

    //堆缓冲区
    @Test
    public void testHeapBuffer() throws UnsupportedEncodingException {
        //取的堆内存
        ByteBuf byteBufHeap = ByteBufAllocator.DEFAULT.heapBuffer();
        byteBufHeap.writeBytes("hello world".getBytes(UTF_8));
        //判断是否是堆内存
        if (byteBufHeap.hasArray()) {
            //取得内部数组
            byte[] array = byteBufHeap.array();
            int offset = byteBufHeap.arrayOffset() + byteBufHeap.readerIndex();
            int length = byteBufHeap.readableBytes();
            System.out.println(new String(array, offset, length));
        }
        byteBufHeap.release();
    }

    //直接缓冲区
    @Test
    public void testDirectsBuffer() throws UnsupportedEncodingException {
        //取的直接内存
        ByteBuf byteBufDirect = ByteBufAllocator.DEFAULT.directBuffer();
        byteBufDirect.writeBytes("hello nanjing".getBytes(UTF_8));
        if (!byteBufDirect.hasArray()) {
            int length = byteBufDirect.readableBytes();
            byte[] array = new byte[length];
            //把数据读取到堆内存
            byteBufDirect.getBytes(byteBufDirect.readerIndex(), array);
            System.out.println(new String(array, UTF_8));
        }
        byteBufDirect.release();
    }
}
