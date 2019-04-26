package com.gliesereum.notification.config.properties;

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
@ConfigurationProperties(prefix = "firebase.properties")
public class FirebaseProperties {

    @NotBlank
    private String credentialPath;

    @NotBlank
    private String appName;
}
