package com.zbl.httpclienttest.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author zbl
 * @version 1.0
 * @since 2022/1/2 22:16
 */
@Slf4j
public class HttpClientUtil {
    private static final HttpClientBuilder httpClientBuilder = HttpClients.custom();

    static {
        /*
         * 绕过不安全的https
         */
        /*Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", trustHttpsCertificates())
                .build();*/

        /*
         * 创建连接池
         */
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(/*registry*/);
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);
        httpClientBuilder.setConnectionManager(cm);

        /*
         * 设置请求默认配置
         */
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);

    }

    /**
     * 构造安全连接工厂
     *
     * @return {@link SSLConnectionSocketFactory}
     */
    private static ConnectionSocketFactory trustHttpsCertificates() {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        try {
            sslContextBuilder.loadTrustMaterial(null, (chain, authType) -> true);
            SSLContext sslContext = sslContextBuilder.build();
            return new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"},
                    null,
                    NoopHostnameVerifier.INSTANCE
            );
        } catch (Exception e) {
            log.error("构造安全连接工厂失败", e);
            throw new RuntimeException("构造安全连接工厂失败");
        }
    }


    public static String get(String url) {
        return get(url, null, null);
    }

    /**
     * 执行get请求
     *
     * @param url     请求的url
     * @param headers 请求头
     * @param params  请求url拼接参数
     * @return 字符串格式的响应内容
     */
    public static String get(String url, Map<String, String> headers, Map<String, String> params) {
        if (url == null || url.length() == 0)
            throw new IllegalArgumentException("url");

        CloseableHttpClient httpClient = httpClientBuilder.build();

        //拼接参数
        if (params != null && params.size() > 0) {
            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            url = sb.toString();
        }
        HttpGet httpGet = new HttpGet(url);
        //设置请求头
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }

        CloseableHttpResponse httpResponse = null;
        try {
            log.info("执行get请求之前url: {}", url);
            httpResponse = httpClient.execute(httpGet);
            log.info("请求之后statusCode: {}", httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(httpResponse);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return null;
    }

    public static String postJson(String url, String body) {
        return postJson(url, body, null);
    }

    public static String postJson(String url, String body, Map<String, String> params) {
        return postJson(url, body, params, null);
    }

    public static String postJson(String url, String body, Map<String, String> params, Map<String, String> headers) {
        if (url == null || url.length() == 0)
            throw new IllegalArgumentException("please key in correct url");

        CloseableHttpClient httpClient = httpClientBuilder.build();

        //拼接参数
        if (params != null && params.size() > 0) {
            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            url = sb.toString();
        }

        HttpPost httpPost = new HttpPost(url);

        //设置请求头
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8),
                        URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            }
        }

        //设置请求体
        if (StringUtils.hasText(body)) {
            StringEntity stringEntity = new StringEntity(body, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse httpResponse = null;
        try {
            log.info("执行post请求之前url: {}", httpPost.getURI());
            log.info("执行post请求之前body: {}", EntityUtils.toString(httpPost.getEntity()));
            httpResponse = httpClient.execute(httpPost);
            log.info("请求之后statusCode: {}", httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                String responseStr = EntityUtils.toString(entity);
                log.info("post responseStr: {}", responseStr);
                return responseStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(httpResponse);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return null;
    }

}
