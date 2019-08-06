package com.gliesereum.share.common.model.dto.karma.record.search;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class BusinessRecordSearchDto {

    private List<StatusRecord> status;

    private List<StatusProcess> processes;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime from;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime to;

    private List<UUID> workingSpaceIds;

    private List<UUID> businessIds;

    private UUID corporationId;

    private List<UUID> clientIds;

    private Integer recordNumber;

    @Min(0)
    private Integer page;

    @Min(1)
    private Integer size;

    private Sort.Direction sortDirection;

    private BusinessRecordSearchOrderField sortField;

}