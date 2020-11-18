package com.netty.chat.server.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private URL baseURL = HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();

    private final static String WEB_ROOT = "webroot";

    private File getFileFromRoot(String fileName) throws URISyntaxException {
        String path = baseURL.toURI().getPath() + WEB_ROOT + "/" + fileName;
        return new File(path);
    }

    @Override
    //read0 netty中 只要方法名称后加0的,都是实现类的方法 只需在此实现逻辑
    protected void channelRead0(ChannelHandlerContext chc, FullHttpRequest request) throws Exception {
        System.out.println("httpHandler...");
        //获取客户端请求的url
        String uri = request.uri();
        String page = uri.equals("/") ? "chat.html" : uri;
        RandomAccessFile file;
        try {
            file = new RandomAccessFile(getFileFromRoot(page), "r");
        } catch (Exception e) {
            chc.fireChannelRead(request.retain());
            return;
        }
        String contextType = "text/html;";
        if (uri.endsWith(".css")) {
            contextType = "text/css;";
        } else if (uri.endsWith(".js")) {
            contextType = "text/javascript;";
        } else if (uri.toLowerCase().matches("(jpg|png|gif)$")) {
            String ext = uri.substring(uri.lastIndexOf("."));
            contextType = "image/" + ext + ";";
        }
        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, contextType + "charset=utf-8;");

        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        if (keepAlive) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        chc.write(response);
        chc.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        //清空缓冲区
        ChannelFuture cf = chc.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            cf.addListener(ChannelFutureListener.CLOSE);
        }
        file.close();
    }
}