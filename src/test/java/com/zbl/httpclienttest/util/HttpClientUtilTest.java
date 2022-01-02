package com.zbl.httpclienttest.util;

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
}
