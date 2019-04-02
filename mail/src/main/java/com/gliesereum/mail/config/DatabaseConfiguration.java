package com.gliesereum.mail.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author vitalij
 * @version 1.0
 */
@Configuration
@EnableJpaRepositories("com.gliesereum.mail.model.repository.jpa")
public class DatabaseConfiguration {
}
