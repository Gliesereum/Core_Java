package com.gliesereum.karma.config.es.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class CustomEntityMapper implements EntityMapper {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<Map<String, Object>>() {
    };

    private final ObjectMapper objectMapper;

    public CustomEntityMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String mapToString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    @Override
    public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
        return objectMapper.readValue(source, clazz);
    }

    // mapObject/readObject were added to EntityMapper in spring-data-elasticsearch
    // 3.2. They are the same conversion as above without the string round trip.

    @Override
    public Map<String, Object> mapObject(Object source) {
        return objectMapper.convertValue(source, MAP_TYPE);
    }

    @Override
    public <T> T readObject(Map<String, Object> source, Class<T> targetType) {
        return objectMapper.convertValue(source, targetType);
    }
}
