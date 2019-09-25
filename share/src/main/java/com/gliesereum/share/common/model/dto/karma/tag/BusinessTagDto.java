package com.gliesereum.share.common.model.dto.karma.tag;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessTagDto extends DefaultDto {

    private UUID businessId;

    private UUID tagId;

    public BusinessTagDto(UUID businessId, UUID tagId) {
        this.businessId = businessId;
        this.tagId = tagId;
    }
}
