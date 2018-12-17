package com.gliesereum.proxy.config.security.properties;

import com.gliesereum.share.common.security.jwt.properties.JwtSecurityProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 16/10/2018
 */

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "security.properties")
public class SecurityProperties extends JwtSecurityProperties {

    @NotBlank
    private String checkAccessUrl;

    @NotBlank
    private String bearerHeader;

    @NotBlank
    private String bearerPrefix;

    @NotNull
    private Boolean endpointKeeperEnable;

    @NotBlank
    private String getUserGroupUrl;

    @NotBlank
    private String getPermissionMapUrl;
}