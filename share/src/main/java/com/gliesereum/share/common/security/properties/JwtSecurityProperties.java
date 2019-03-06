package com.gliesereum.share.common.security.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "security.properties")
public class JwtSecurityProperties {

    @NotBlank
    private String jwtHeader;

    @NotBlank
    private String jwtPrefix;

    @NotBlank
    private String jwtSecret;
}
