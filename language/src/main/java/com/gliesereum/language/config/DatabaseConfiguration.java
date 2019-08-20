package com.gliesereum.language.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author vitalij
 */
@Configuration
@EnableJpaRepositories({
        "com.gliesereum.language.model.repository.jpa",
        "com.gliesereum.share.common.repository.refreshable"})
public class DatabaseConfiguration {
}
