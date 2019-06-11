package com.gliesereum.account.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Configuration
@EnableJpaRepositories("com.gliesereum.account.model.repository")
@EnableJpaAuditing
public class DatabaseConfiguration {
}
