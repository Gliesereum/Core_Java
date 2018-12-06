package com.gliesereum.karma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = {
		"com.gliesereum.karma",
		"com.gliesereum.share.common.exception.handler",
		"com.gliesereum.share.common.security.jwt"})
@EnableEurekaClient
public class KarmaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KarmaApplication.class, args);
	}
}
