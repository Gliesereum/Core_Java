package com.gliesereum.socket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.share.common.exchange.service.auth.AuthExchangeService;
import com.gliesereum.share.common.exchange.service.auth.impl.AuthExchangeServiceImpl;
import com.gliesereum.share.common.security.bearer.filter.BearerAuthenticationFilter;
import com.gliesereum.share.common.security.handler.ExceptionHandlerFilter;
import com.gliesereum.share.common.security.jwt.factory.JwtTokenFactory;
import com.gliesereum.share.common.security.jwt.factory.impl.JwtTokenFactoryImpl;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackageClasses = BearerAuthenticationFilter.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BearerAuthenticationFilter bearerAuthenticationFilter;

    @Bean
    public JwtTokenFactory jwtTokenFactory(SecurityProperties securityProperties, ObjectMapper objectMapper) {
        return new JwtTokenFactoryImpl(securityProperties, objectMapper);
    }

    @Bean
    public AuthExchangeService authService(RestTemplate restTemplate, SecurityProperties securityProperties) {
        return new AuthExchangeServiceImpl(restTemplate, securityProperties);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .anonymous().disable()
                .csrf().disable()
                .requestMatchers().antMatchers("/**")
                .and().authorizeRequests().anyRequest().permitAll()
                .and()
                .addFilterBefore(bearerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), BearerAuthenticationFilter.class);
    }
}
