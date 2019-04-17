package com.gliesereum.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author vitalij
 * @version 1.0
 */
@Configuration
@EnableJpaRepositories("com.gliesereum.notification.model.repository.jpa")
public class DatabaseConfiguration {

}
