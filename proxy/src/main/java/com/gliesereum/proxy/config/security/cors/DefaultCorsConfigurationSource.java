package com.gliesereum.proxy.config.security.cors;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 16/10/2018
 */
@Component
public class DefaultCorsConfigurationSource implements CorsConfigurationSource {

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        return null;
    }
}
