package com.zbl.httpclienttest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class HttpClientTestApplicationTests {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void contextLoads() {
        System.out.println(restTemplate);
    }

}
