package com.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/29 16:23
 */
public class WriteReadTest {
    @Test
    public void testWriteRead() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        System.out.println("分配byteBuf....");
        byteBuf.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("写入4个字节{1,2,3,4}");

        System.out.println("start get================");
        getByteBuf(byteBuf);
        System.out.println("动作:取完数据 ByteBuf");

        System.out.println("start read================");
        readByteBuf(byteBuf);
        System.out.println("动作:读完数据 ByteBuf");

    }

    //取字节
    private void readByteBuf(ByteBuf byteBuf) {
        while (byteBuf.isReadable()) {
            System.out.println("取一个字节:" + byteBuf.readByte());
        }
    }

    //读字节,不改变指针
    private void getByteBuf(ByteBuf byteBuf) {
        for (int i = 0; i < byteBuf.readableBytes(); i++) {
            System.out.println("读一个字节："+byteBuf.getByte(i));
        }
    }
}
