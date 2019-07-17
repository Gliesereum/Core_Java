package com.gliesereum.share.common.model.dto.karma.feedback;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkingSpaceDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FeedBackUserDto extends DefaultDto {

    private UUID objectId;

    private UUID clientId;

    private UUID businessId;

    private UUID workerId;

    private UUID workingSpaceId;

    private PublicUserDto client;

    private LiteBusinessDto business;

    private LiteWorkerDto workerDto;

    private LiteWorkingSpaceDto workingSpaceDto;

    private String comment;

    @Min(value = 1)
    @Max(value = 5)
    private Integer mark;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime dateFeedback;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime dateCreateObject;
}