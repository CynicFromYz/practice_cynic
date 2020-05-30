package com.netty.netty;

import com.sun.org.apache.xpath.internal.operations.String;
import io.netty.buffer.*;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/27 15:30
 */
public class NettyDemo {

    public static void main(String[] args) {
        UnpooledHeapByteBuf heapByteBuf;
        PooledByteBufAllocator.DEFAULT.directArenas();
        UnpooledByteBufAllocator.DEFAULT.directBuffer();
    }

}
