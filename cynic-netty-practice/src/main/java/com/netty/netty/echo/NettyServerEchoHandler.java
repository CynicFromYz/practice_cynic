package com.netty.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/30 14:27
 */
@ChannelHandler.Sharable
public class NettyServerEchoHandler extends ChannelInboundHandlerAdapter {
    public static NettyServerEchoHandler instance = new NettyServerEchoHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int length = byteBuf.readableBytes();
        byte[] array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array);
        //写回数据,异步任务
        ChannelFuture future = ctx.writeAndFlush(msg);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("数据写回....");
            }
        });
    }
}
