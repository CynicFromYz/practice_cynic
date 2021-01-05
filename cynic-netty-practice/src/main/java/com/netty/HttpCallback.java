package com.netty;

import org.apache.http.client.methods.CloseableHttpResponse;

public interface HttpCallback {
    String getResponseContent(CloseableHttpResponse response);
}
