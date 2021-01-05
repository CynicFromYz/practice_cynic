package com.netty;

import java.io.IOException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class DefaultCallbackImpl implements HttpCallback {

    public String getResponseContent(CloseableHttpResponse response) {
        HttpEntity entity = response.getEntity();
        try {
            return EntityUtils.toString(entity, Consts.UTF_8);
        } catch (Exception e) {
            return "";
        } finally {
            try {
                response.close();
            } catch (IOException e) {
            }
        }
    }

}
