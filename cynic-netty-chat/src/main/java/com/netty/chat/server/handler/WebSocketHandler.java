package com.netty.chat.server.handler;

import com.netty.chat.process.IMProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private IMProcessor imProcessor = new IMProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        imProcessor.process(channelHandlerContext.channel(), msg.text());
        System.out.println(msg.text());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        imProcessor.logout(ctx.channel());
        System.out.println("有人退出...");
    }
}
