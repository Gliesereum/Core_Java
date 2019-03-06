package com.gliesereum.proxy;

import com.gliesereum.share.common.security.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableZuulProxy
@EnableEurekaClient
@EnableCircuitBreaker
@EnableConfigurationProperties(SecurityProperties.class)
@SpringBootApplication(scanBasePackages = {
		"com.gliesereum.proxy",
		"com.gliesereum.share.common.exception.handler"})
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}
}
