package com.gliesereum.karma.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author vitalij
 * @version 1.0
 * @since 04/12/2018
 */
@Configuration
@EnableJpaRepositories("com.gliesereum.karma.model.repository")
public class DatabaseConfiguration {
}
