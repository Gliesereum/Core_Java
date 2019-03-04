package com.gliesereum.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = {
        "com.gliesereum.mail",
        "com.gliesereum.share.common.exception.handler",
        "com.gliesereum.share.common.security.jwt"})
@EnableEurekaClient
public class MailApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class, args);
    }
}
