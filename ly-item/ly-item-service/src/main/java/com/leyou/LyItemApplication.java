package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author ssqswyf
 * @Date 2021/4/18
 */

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.leyou.item.mapper")
public class LyItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyItemApplication.class,args);
    }
}
