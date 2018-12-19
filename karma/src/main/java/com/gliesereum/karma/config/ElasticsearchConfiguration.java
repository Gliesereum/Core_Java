package com.gliesereum.karma.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-17
 */

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.gliesereum.karma.model.repository.es")
public class ElasticsearchConfiguration {

}
