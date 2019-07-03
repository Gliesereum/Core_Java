package com.gliesereum.license.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author vitalij
 */
@Configuration
@EnableJpaRepositories("com.gliesereum.license.model.repository.jpa")
public class DatabaseConfiguration {
}
