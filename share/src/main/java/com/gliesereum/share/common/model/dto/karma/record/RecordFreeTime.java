package com.gliesereum.share.common.model.dto.karma.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class RecordFreeTime {

    private UUID workingSpaceID;

    private UUID workerID;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime begin;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime finish;

    private Long min;

    public RecordFreeTime(UUID workingSpaceID, LocalDateTime begin, LocalDateTime finish) {
        this.workingSpaceID = workingSpaceID;
        this.begin = begin;
        this.finish = finish;
    }

    public RecordFreeTime(UUID workingSpaceID, UUID workerID, LocalDateTime begin, LocalDateTime finish) {
        this.workingSpaceID = workingSpaceID;
        this.workerID = workerID;
        this.begin = begin;
        this.finish = finish;
    }

    public Long getMin() {
        setMin(Duration.between(begin, finish).toMinutes());
        return min;
    }

    public void setMin(Long min) {
        this.min = min;
    }
}
