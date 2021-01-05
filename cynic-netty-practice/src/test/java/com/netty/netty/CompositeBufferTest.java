package com.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/30 11:07
 */
public class CompositeBufferTest {
    final static Charset UTF_8 = Charset.forName("UTF-8");

    @Test
    public void byteBufComposite() {
        CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        //消息头
        ByteBuf headerBuf = Unpooled.copiedBuffer("hello world", UTF_8);
        //消息体1
        ByteBuf bodyBuf = Unpooled.copiedBuffer("hello nanjing", UTF_8);
        compositeByteBuf.addComponents(headerBuf, bodyBuf);
        sendMsg(compositeByteBuf);
        //在refCnt为0前,retain
        headerBuf.retain();
        compositeByteBuf.release();
        compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        //消息体2
        bodyBuf = Unpooled.copiedBuffer("hello cynic", UTF_8);
        compositeByteBuf.addComponents(headerBuf, bodyBuf);
        sendMsg(compositeByteBuf);
        compositeByteBuf.release();

    }

    private void sendMsg(CompositeByteBuf byteBufs) {
        //处理整个消息
        for (ByteBuf byteBuf : byteBufs) {
            int length = byteBuf.readableBytes();
            byte[] array = new byte[length];
            //将CompositeByteBuf中的数据复制到数组中
            byteBuf.getBytes(byteBuf.readerIndex(), array);
            //处理数组中的数据
            System.out.println(new String(array, UTF_8));
        }
    }
}
