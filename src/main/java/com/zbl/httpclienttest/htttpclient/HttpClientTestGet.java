package com.zbl.httpclienttest.htttpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author zbl
 * @version 1.0
 * @since 2022/1/2 10:43
 */
@Slf4j
public class HttpClientTestGet {

    public static void main(String[] args) {
        //创建http请求客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String url = "http://127.0.0.1:8080/http-test/get";

        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(2000) //链接建立的超时时间；
                .setConnectionRequestTimeout(10000) //响应超时时间，超过此时间不再读取响应
                .setSocketTimeout(4000) //http clilent中从connetcion pool中获得一个connection的超时时间
                .build();
        httpGet.setConfig(config);

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String responseStr = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                log.info("响应结果：:{}", responseStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
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
