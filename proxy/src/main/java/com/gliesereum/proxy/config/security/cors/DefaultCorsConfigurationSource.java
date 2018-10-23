package com.gliesereum.proxy.config.security.cors;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 16/10/2018
 */
@Component
public class DefaultCorsConfigurationSource extends UrlBasedCorsConfigurationSource {

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList("*");
    private static final List<String> ALLOWED_METHODS = Arrays.asList("GET", "POST", "PUT", "DELETE");

    public DefaultCorsConfigurationSource() {
        super();
        super.registerCorsConfiguration("/**", corsConfiguration());
    }

    private CorsConfiguration corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        configuration.setAllowedMethods(ALLOWED_METHODS);
        return configuration;
    }
}
