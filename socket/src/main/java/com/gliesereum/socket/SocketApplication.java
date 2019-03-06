package com.gliesereum.socket;

import com.gliesereum.share.common.security.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(
        scanBasePackages = {
                "com.gliesereum.socket",
                "com.gliesereum.share.common.exception.handler"})
@EnableAsync
@EnableEurekaClient
@EnableConfigurationProperties(SecurityProperties.class)
public class SocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocketApplication.class, args);
    }
}
