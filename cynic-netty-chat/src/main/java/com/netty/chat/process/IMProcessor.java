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
            JSONObject attrs = getAttrs(client);
            long currTime = System.currentTimeMillis();
            if (null != attrs) {
                long lastTime = attrs.getLongValue("lastFlowerTime");
                //10s内不允许重复刷鲜花
                int seconds = 10;
                long sub = currTime - lastTime;
                if (sub < 1000 * seconds) {
                    msgRequest.setSender("you");
                    msgRequest.setCmd(IMP.SYSTEM.getName());
                    msgRequest.setContent("您送鲜花太频繁," + (seconds - Math.round(sub / 1000) + "后可再次送花!"));
                    String content = encoder.encode(msgRequest);
                    client.writeAndFlush(new TextWebSocketFrame(content));
                    return;
                }
            }
            //正常送花
            for (Channel channel : onlineUsers) {
                if (channel == client) {
                    msgRequest.setSender("you");
                    msgRequest.setContent("你送了大家一波鲜花雨");
                    //设置最后一次刷鲜花的时间,刷鲜花不能太频繁
                    setAttrs(client, "lastFlowerTime", currTime);
                } else {
                    msgRequest.setSender(nickName);
                    msgRequest.setContent(nickName + "送来一波鲜花!");
                }
                msgRequest.setTime(System.currentTimeMillis());
                String content = encoder.encode(msgRequest);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
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

    public void setAttrs(Channel client, String key, long currTime) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, currTime);
        client.attr(ATTRS).set(jsonObject);
    }
}
