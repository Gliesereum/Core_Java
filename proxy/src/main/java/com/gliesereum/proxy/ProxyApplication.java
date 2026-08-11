package com.gliesereum.proxy;

import com.gliesereum.share.common.security.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Note the scan no longer reaches com.gliesereum.share.common.exception.handler:
 * RestExceptionHandler in that package is a servlet @ControllerAdvice built on
 * HttpServletRequest and ServletUriComponentsBuilder, which cannot load on the
 * reactive stack. GatewayErrorWebExceptionHandler renders the same error body
 * instead.
 */
@EnableAsync
@EnableEurekaClient
@EnableConfigurationProperties(SecurityProperties.class)
@SpringBootApplication(scanBasePackages = "com.gliesereum.proxy")
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}
}
