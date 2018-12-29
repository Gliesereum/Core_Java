package com.gliesereum.share.common.databind.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @author vitalij
 * @version 1.0
 * @since 2018-12-28
 */
public class LocalTimeJsonDeserializer extends JsonDeserializer<LocalTime> {
    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Long millis = p.getValueAsLong();
        LocalTime result = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalTime();
        return result;
    }
}
