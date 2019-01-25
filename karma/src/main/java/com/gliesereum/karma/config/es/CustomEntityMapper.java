package com.gliesereum.karma.config.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class CustomEntityMapper implements EntityMapper {
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
}
