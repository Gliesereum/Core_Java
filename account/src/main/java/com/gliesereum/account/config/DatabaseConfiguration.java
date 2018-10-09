package com.gliesereum.account.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
@Configuration
@EnableJpaRepositories("com.gliesereum.account.model.repository")
public class DatabaseConfiguration {
}
