package com.gliesereum.file.config.cdn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "cdn.properties")
public class CdnProperties {

    @NotEmpty
    private String accessKey;

    @NotEmpty
    private String secretKey;

    @NotEmpty
    private String host;

    @NotEmpty
    private String region;

    @NotEmpty
    private String bucket;

    @NotEmpty
    private String folder;
}
