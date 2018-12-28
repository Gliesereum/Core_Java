package com.gliesereum.media;

import com.gliesereum.media.config.cdn.CdnProperties;
import com.gliesereum.share.common.security.jwt.properties.JwtSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.gliesereum.media",
        "com.gliesereum.share.common.exception.handler",
        "com.gliesereum.share.common.security.jwt"})
@EnableAsync
@EnableScheduling
@EnableEurekaClient
@EnableConfigurationProperties({JwtSecurityProperties.class, CdnProperties.class})
public class MediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);
    }
}
