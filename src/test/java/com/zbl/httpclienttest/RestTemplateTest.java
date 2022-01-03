package com.zbl.httpclienttest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author zbl
 * @version 1.0
 * @since 2022/1/3 20:25
 */
@Slf4j
@SpringBootTest
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testGet() {
        String url = "https://www.baidu.com";
        String responseStr = restTemplate.getForObject(url, String.class);
        log.info("get response : {}", responseStr);
    }

    @Test
    public void testGet2() {
        String url = "http://localhost:8080/http-test/get";
        String responseStr = restTemplate.getForObject(url, String.class);
        log.info("get response : {}", responseStr);
    }

    @Test
    public void testGet3() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "是快乐的家");
        map.put("age", "78");
        String url = "http://localhost:8080/http-test/get-with-param?name={name}&age={age}";
        String responseStr = restTemplate.getForObject(url, String.class, map);
        log.info("get response : {}", responseStr);
    }
}
