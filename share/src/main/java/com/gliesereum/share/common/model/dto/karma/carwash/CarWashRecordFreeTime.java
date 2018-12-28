package com.gliesereum.share.common.model.dto.karma.carwash;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
public class CarWashRecordFreeTime {

    private UUID workingSpaceID;

    private LocalDateTime begin;

    private LocalDateTime finish;

    private Long min;

    public CarWashRecordFreeTime(UUID workingSpaceID, LocalDateTime begin, LocalDateTime finish) {
        this.workingSpaceID = workingSpaceID;
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
