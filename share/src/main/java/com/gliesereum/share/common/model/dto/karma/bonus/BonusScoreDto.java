package com.gliesereum.share.common.model.dto.karma.bonus;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BonusScoreDto extends DefaultDto {

    private UUID userId;

    private Double score;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime createDate;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime updateDate;

    private List<BonusScoreHistoryDto> history = new ArrayList<>();
}
