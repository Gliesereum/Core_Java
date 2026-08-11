package com.gliesereum.proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Same CORS contract the servlet DefaultCorsConfigurationSource applied, moved
 * onto the reactive stack. It used to be attached through Spring Security's
 * .cors(); the gateway no longer pulls in spring-security, so it is a plain
 * CorsWebFilter now.
 */
@Configuration
public class CorsWebFluxConfiguration {

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList("*");
    private static final List<String> ALLOWED_METHODS = Arrays.asList("GET", "POST", "PUT", "DELETE");
    private static final List<String> ALLOWED_HEADERS =
            Arrays.asList("Authorization", "Accept", "Content-Type", "x-compress", "Application-Id");

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsWebFilter(source);
    }
}
