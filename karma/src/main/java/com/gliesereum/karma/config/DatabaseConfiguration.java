package com.gliesereum.karma.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author vitalij
 * @version 1.0
 */
@Configuration
@EnableJpaRepositories({"com.gliesereum.karma.model.repository.jpa",
                        "com.gliesereum.share.common.repository.refreshable"})
public class DatabaseConfiguration {
}
