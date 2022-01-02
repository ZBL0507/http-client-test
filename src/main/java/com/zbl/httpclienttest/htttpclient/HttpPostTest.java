package com.zbl.httpclienttest.htttpclient;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * @author zbl
 * @version 1.0
 * @since 2022/1/2 13:19
 */
@Slf4j
public class HttpPostTest {
    public static void main(String[] args) {
        try {
            testPostTimeout();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    public static void testPostTimeout() throws URISyntaxException {
        //创建客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //建立uri
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(8080)
                .setPath("/http-test/post-timeout")
                .build();

        //请求配置
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setSocketTimeout(4000)
                .setConnectionRequestTimeout(3000)
                .build();

        //post请求
        HttpPost httpPost = new HttpPost(uri);
        //设置配置
        httpPost.setConfig(config);

        log.info("uri is : {}", httpPost.getURI());

        HashMap<String, String> map = new HashMap<>();
        map.put("name", "施罗科夫");
        map.put("age", "78");
        map.put("sex", "female");
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(map), ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                String responseStr = EntityUtils.toString(entity);
                log.info("responseStr: {}", responseStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
