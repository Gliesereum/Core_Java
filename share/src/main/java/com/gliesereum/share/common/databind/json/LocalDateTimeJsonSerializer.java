package com.gliesereum.share.common.databind.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 17/10/2018
 */
public class LocalDateTimeJsonSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            long millis = value.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
            gen.writeNumber(millis);
        } else {
            gen.writeNull();
        }
    }
}
