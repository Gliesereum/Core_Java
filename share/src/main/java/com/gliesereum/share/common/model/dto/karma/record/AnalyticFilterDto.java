package com.gliesereum.share.common.model.dto.karma.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
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
public class AnalyticFilterDto {

    private UUID businessId;

    private UUID corporationId;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime from;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime to;

    private List<UUID> clientIds;

    private List<UUID> carBrandIds;

    private List<UUID> carModelIds;

    private List<UUID> serviceIds;

    private List<UUID> packageIds;

    private List<UUID> workerIds;

    private List<UUID> serviceClassIds;
}