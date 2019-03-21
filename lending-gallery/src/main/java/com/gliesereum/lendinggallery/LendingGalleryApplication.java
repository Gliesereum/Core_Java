package com.gliesereum.lendinggallery;

import com.gliesereum.share.common.security.properties.JwtSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(
		scanBasePackages = {
				"com.gliesereum.lendinggallery",
				"com.gliesereum.share.common.exception.handler",
				"com.gliesereum.share.common.security.jwt"},
		exclude = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})
@EnableAsync
@EnableEurekaClient
@EnableConfigurationProperties(JwtSecurityProperties.class)
public class LendingGalleryApplication {

	public static void main(String[] args) {
		System.out.println(12 % 6);
		SpringApplication.run(LendingGalleryApplication.class, args);
	}
}
