package com.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/30 13:51
 */
public class SliceTest {
    @Test
    public void testSlice() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        System.out.println("动作:分配 ByteBuf(9,100)");
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println("动作:写入4个字节(1,2,3,4)");
        ByteBuf slice = buffer.slice(0,2);
        buffer.retain();
        byte[] array = new byte[slice.readableBytes()];
        slice.getBytes(slice.readerIndex(), array);
        for (byte b : array) {
            System.out.println(b);
        }
        System.out.println("动作:切片 slice");
        buffer.release();
    }
}
