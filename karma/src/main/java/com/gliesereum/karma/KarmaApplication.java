package com.gliesereum.karma;

import com.gliesereum.share.common.security.jwt.properties.JwtSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {
		"com.gliesereum.karma",
		"com.gliesereum.share.common.exception.handler",
		"com.gliesereum.share.common.security.jwt"})
@EnableAsync
@EnableEurekaClient
@EnableConfigurationProperties(JwtSecurityProperties.class)
public class KarmaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KarmaApplication.class, args);
	}
}
