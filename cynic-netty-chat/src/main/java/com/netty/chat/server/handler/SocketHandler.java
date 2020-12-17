package com.netty.chat.server.handler;

import com.netty.chat.process.IMProcessor;
import com.netty.chat.protocol.IMMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SocketHandler extends SimpleChannelInboundHandler<IMMessage> {

    private IMProcessor processor = new IMProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IMMessage imMessage) throws Exception {
        processor.process(channelHandlerContext.channel(), imMessage);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有客户端连接");
    }
}
