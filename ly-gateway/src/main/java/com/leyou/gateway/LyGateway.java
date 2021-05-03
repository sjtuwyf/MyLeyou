package com.leyou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Author ssqswyf
 * @Date 2021/4/17
 */
@EnableZuulProxy
@SpringCloudApplication
public class LyGateway {

    public static void main(String[] args) {
        SpringApplication.run(LyGateway.class);
    }
}
