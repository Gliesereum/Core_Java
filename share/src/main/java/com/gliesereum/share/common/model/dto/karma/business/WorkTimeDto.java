package com.gliesereum.share.common.model.dto.karma.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkTimeDto extends DefaultDto {

    @NotNull
    @JsonDeserialize(using = LocalTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalTimeJsonSerializer.class)
    private LocalTime from;

    @NotNull
    @JsonDeserialize(using = LocalTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalTimeJsonSerializer.class)
    private LocalTime to;

    private UUID objectId;

    private Boolean isWork;

    private UUID businessCategoryId;

    private DayOfWeek dayOfWeek;
}
