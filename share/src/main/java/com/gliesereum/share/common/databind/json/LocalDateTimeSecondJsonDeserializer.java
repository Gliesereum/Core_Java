package com.gliesereum.share.common.databind.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class LocalDateTimeSecondJsonDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        long second = p.getValueAsLong();
        return Instant.ofEpochSecond(second).atZone(ZoneId.of("UTC")).toLocalDateTime();
    }
}
