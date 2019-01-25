package com.gliesereum.share.common.databind.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @author vitalij
 * @version 1.0
 */
public class LocalTimeJsonSerializer extends JsonSerializer<LocalTime> {
    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            long millis = LocalDateTime.of(LocalDate.now(), value).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
            gen.writeNumber(millis);
        } else {
            gen.writeNull();
        }
    }
}
