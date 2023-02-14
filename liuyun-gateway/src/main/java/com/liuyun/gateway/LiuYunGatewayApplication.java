package com.liuyun.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * LiuYunGatewayApp
 *
 * @author W.d
 * @since 2022/12/22 21:34
 **/
@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class LiuYunGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiuYunGatewayApplication.class, args);
    }

}
