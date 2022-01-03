package com.zbl.httpclienttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

@SpringBootApplication
public class HttpClientTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpClientTestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        //创建带有超时时间的restTemplate
        RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3)).build();

        //解决中文乱码的问题
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (int i = 0; i < messageConverters.size(); i++) {
            HttpMessageConverter<?> httpMessageConverter = messageConverters.get(i);
            if (httpMessageConverter.getClass().equals(StringHttpMessageConverter.class)) {
                messageConverters.set(i, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            }
        }

        return restTemplate;
    }

}
