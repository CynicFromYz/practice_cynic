package com.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.io.IOException;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/29 15:36
 */
public class PipeLineHotOperateTester {
    static class SimpleInHandlerA extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("入站处理器,A被回调...");
            super.channelRead(ctx, msg);
            ctx.pipeline().remove(this);
        }
    }

    static class SimpleInHandlerB extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("入站处理器,B被回调...");
            super.channelRead(ctx, msg);
        }
    }

    static class SimpleInHandlerC extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("入站处理器,C被回调...");
            super.channelRead(ctx, msg);
        }
    }

    @Test
    public void testPipeLineHotOperating() throws IOException {
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new SimpleInHandlerA());
                ch.pipeline().addLast(new SimpleInHandlerB());
                ch.pipeline().addLast(new SimpleInHandlerC());
            }
        };
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(channelInitializer);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(1);
        //第一次向通道写入报文(或数据包)
        embeddedChannel.writeInbound(byteBuf);
        //第二次向通道写入报文(或数据包)
        embeddedChannel.writeInbound(byteBuf);
        //第三次向通道写入报文(或数据包)
        embeddedChannel.writeInbound(byteBuf);
        System.in.read();
    }

}
