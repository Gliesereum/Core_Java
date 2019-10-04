package com.gliesereum.share.common.model.dto.karma.business.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class BusinessDocumentWorkTimeDto {
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime from;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime to;
    
    private String objectId;
    
    private Boolean isWork;
    
    private UUID businessCategoryId;
    
    private DayOfWeek dayOfWeek;
}
