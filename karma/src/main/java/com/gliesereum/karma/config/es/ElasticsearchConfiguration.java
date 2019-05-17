package com.gliesereum.karma.config.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vanroy.springdata.jest.JestElasticsearchTemplate;
import com.github.vanroy.springdata.jest.mapper.DefaultJestResultsMapper;
import com.github.vanroy.springdata.jest.mapper.JestResultsMapper;
import com.gliesereum.karma.config.es.mapper.CustomEntityMapper;
import com.gliesereum.karma.config.es.mapper.CustomErrorMapper;
import com.gliesereum.karma.config.es.mapper.CustomJestResultMapper;
import io.searchbox.client.JestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.gliesereum.karma.model.repository.es")
public class ElasticsearchConfiguration {

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(JestClient jestClient, ElasticsearchConverter elasticsearchConverter, JestResultsMapper jestResultsMapper) {
        return new JestElasticsearchTemplate(jestClient, elasticsearchConverter, jestResultsMapper, new CustomErrorMapper(), null);
    }

    @Bean
    public SimpleElasticsearchMappingContext mappingContext() {
        return new SimpleElasticsearchMappingContext();
    }

    @Bean
    public ElasticsearchConverter elasticsearchConverter(SimpleElasticsearchMappingContext mappingContext) {
        return new MappingElasticsearchConverter(mappingContext);
    }

    @Bean
    public JestResultsMapper jestResultsMapper(SimpleElasticsearchMappingContext mappingContext, ObjectMapper objectMapper) {
        return new CustomJestResultMapper(mappingContext, new CustomEntityMapper(objectMapper));
    }

}
