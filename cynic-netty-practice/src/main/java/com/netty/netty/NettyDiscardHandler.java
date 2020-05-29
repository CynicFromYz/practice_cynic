package com.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/27 16:42
 */
public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            System.out.println("收到消息,丢弃如下:");
            while (byteBuf.isReadable()) {
                System.out.println((char) byteBuf.readByte());
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
