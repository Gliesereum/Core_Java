package com.gliesereum.share.common.security.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
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

    @NotBlank
    private String applicationIdHeader;

    @NotBlank
    private Boolean applicationIdHeaderRequired;

    private List<String> notRequiredApplicationIdHosts;

    @NotNull
    private Boolean endpointKeeperEnable;

    @NotBlank
    private String getUserGroupUrl;

    @NotBlank
    private String getPermissionMapUrl;

    @NotBlank
    private String checkApplicationId;
}
