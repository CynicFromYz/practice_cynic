package com.netty.chat.process;

import com.alibaba.fastjson.JSONObject;
import com.netty.chat.protocol.IMDecoder;
import com.netty.chat.protocol.IMEncoder;
import com.netty.chat.protocol.IMMessage;
import com.netty.chat.protocol.IMP;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

public class IMProcessor {

    private final static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private IMDecoder decoder = new IMDecoder();
    private IMEncoder encoder = new IMEncoder();

    private final AttributeKey<String> NICK_NAME = AttributeKey.valueOf("nickName");
    private final AttributeKey<String> IP_ADDR = AttributeKey.valueOf("ipAddr");
    private final AttributeKey<JSONObject> ATTRS = AttributeKey.valueOf("attrs");

    public void process(Channel client, String msg) {
        IMMessage msgRequest = decoder.decode(msg);
        if (null == msgRequest) {
            return;
        }

        String nickName = msgRequest.getSender();

        //如果是登录操作  就向onlineUsers中加入一条信息
        if (IMP.LOGIN.getName().equals(msgRequest.getCmd())) {
            client.attr(NICK_NAME).getAndSet(msgRequest.getSender());
            client.attr(IP_ADDR).getAndSet(msgRequest.getAddr());
            onlineUsers.add(client);
            for (Channel channel : onlineUsers) {
                if (channel != client) {
                    msgRequest = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), nickName + "进入了聊天室");
                } else {
                    msgRequest = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), "与服务器建立连接");
                }
                String text = encoder.encode(msgRequest);
                channel.writeAndFlush(new TextWebSocketFrame(text));
            }
        } else if (IMP.LOGOUT.getName().equals(msgRequest.getCmd())) {
            onlineUsers.remove(client);
        } else if (IMP.CHAT.getName().equals(msgRequest.getCmd())) {
            for (Channel channel : onlineUsers) {
                if (channel != client) {
//                    msgRequest.setSender(channel.attr(NICK_NAME).get());
                    msgRequest.setSender(nickName);
                } else {
                    msgRequest.setSender("you");
                }
                String text = encoder.encode(msgRequest);
                channel.writeAndFlush(new TextWebSocketFrame(text));
            }
        } else if (IMP.FLOWER.getName().equals(msgRequest.getCmd())) {
//            msgRequest.setSender(nickName);
//            msgRequest.set
//            for (Channel channel : onlineUsers) {
//                String text = encoder.encode(msgRequest);
//                channel.writeAndFlush(new TextWebSocketFrame(text));
//            }
        }

    }

    public void logout(Channel client) {
        onlineUsers.remove(client);
    }

    private Long sysTime() {
        return new Date().getTime();
    }

    public JSONObject getAttrs(Channel client) {
        return client.attr(ATTRS).get();
    }
}
