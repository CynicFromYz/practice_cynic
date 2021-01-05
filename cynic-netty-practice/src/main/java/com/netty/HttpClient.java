package com.netty;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClient {
    static Logger logger = LoggerFactory.getLogger(HttpClient.class);
    public static final String TENANT_CODE = "cuxiaodms";
    public static final String USER_CODE = "boss";
    public static final String PASSWORD = "a888888";
    public static final String REQUEST_IP = "172.31.3.77:8088";
    public static final String REQUEST_IP_REPLICATION = "172.31.3.62:8080";
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
    public static int count = 0;

    public static String executePost(String url, Map<String, String> params, Header header, HttpCallback callback)
            throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        ++count;
        try {
            if (!url.startsWith("http:")) {
//				if (url.startsWith("/"))
//				{
                if (count % 2 == 0) {
                    url = String.format("http://%s", REQUEST_IP_REPLICATION + url);
                } else {
                    url = String.format("http://%s", REQUEST_IP_REPLICATION + url);
                }

//				}
//				else
//				{
//					url = String.format("http://%s/%s", REQUEST_IP, url);
//				}
            }
            RequestBuilder rb = RequestBuilder.post().setUri(new URI(url));
            if (params != null && !params.isEmpty()) {
                List<BasicNameValuePair> ps = new ArrayList<BasicNameValuePair>();
                for (String key : params.keySet()) {
                    ps.add(new BasicNameValuePair(key, params.get(key)));
                }
                rb.setEntity(new UrlEncodedFormEntity(ps, Consts.UTF_8));
            }
            HttpUriRequest hur = rb.build();
            if (header != null) {
                hur.addHeader(header);
            }
            System.out.println("发送时间" + System.currentTimeMillis());
            return callback.getResponseContent(httpclient.execute(hur));
        } finally {
            httpclient.close();
        }
    }

    public static String executePost(String url, Map<String, String> params) throws Exception {
        Header header = new Auth(TENANT_CODE, USER_CODE, PASSWORD).getAuthHeader();
        return executePost(url, params, header, new DefaultCallbackImpl());
    }

    public static String executePost(String url, Map<String, String> params, Auth auth) throws Exception {
        return executePost(url, params, auth.getAuthHeader(), new DefaultCallbackImpl());
    }

    public static String executePost(String url) throws Exception {
        return executePost(url, null);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        logger.info("test");
        Map<String, String> params = new HashMap<String, String>();
        params.put("activity.id", "6409628450096308372");
        params.put("activity.activityStatus", "1");
        params.put("activity.creatorId", "5478903775380438019");
        params.put("query.activity_type", "1");
        params.put("activity.auditReason", "");
        params.put("token", "64989233421243114432231644372");
        for (int i = 0; i < 100; i++) {
            Thread.sleep(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "线程已准备就绪!");
                        cyclicBarrier.await();
                        try {
                            executePost("/app/cuxiao/web/activity/activity_auditActivity.action", params);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
//		String rt =
//		System.out.println("----------------------------------------");
//		System.out.println(String.format("Response content:%s", rt));
//		System.out.println("----------------------------------------");
    }
}
