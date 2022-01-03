package com.zbl.httpclienttest.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zbl
 * @version 1.0
 * @since 2022/1/2 23:02
 */
@Slf4j
public class HttpClientUtilTest {

    @Test
    public void testGet() {
        String url = "https://www.baidu.com";
        String responseStr = HttpClientUtil.get(url);
        log.info("响应内容: {}", responseStr);
    }

    @Test
    public void testGetWithParam() {
        String url = "http://localhost:8080/http-test/get-with-param";
        Map<String, String> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", "34");
        String str = HttpClientUtil.get(url, null, map);
        log.info("响应内容: {}", str);
    }

    @Test
    public void testPost() {
        String url = "http://localhost:8080/http-test/post-timeout";
        Map<String, String> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", "34");

        HashMap<String, String> params = new HashMap<>();
        params.put("我是请求参数", "哈哈哈哈哈");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("请求头请求头", "头头头头头");
        headers.put("aaaakey", "bbbbvalue");

        String json = HttpClientUtil.postJson(url, JSON.toJSONString(map), params, headers);
        log.info("post response is : {}", json);
    }
}
