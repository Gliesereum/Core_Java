package com.gliesereum.share.common.model.dto.karma.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class RecordsSearchDto {

    private StatusRecord status;

    private ServiceType serviceType;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime from;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime to;

    private List<UUID> workingSpaceIds;

    private List<UUID> targetIds;

    private List<UUID> businessIds;

}
