package com.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.io.IOException;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/29 14:28
 */
public class InHandlerDemoTester {

    @Test
    public void testInHandlerLifeCirce() throws InterruptedException, IOException {
        final InHandlerDemo inHandler = new InHandlerDemo();
        //初始化处理器
        ChannelInitializer<EmbeddedChannel> channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(inHandler);
            }
        };
        //创建嵌入式通道
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(1);
        //模拟入站,写一个入站数据包
        channel.writeInbound(byteBuf);
        channel.flush();
        //通道关闭
        channel.close();
        System.in.read();
    }
}
