package com.gliesereum.share.common.model.dto.karma.tag;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ObjectTagDto extends DefaultDto {

    private UUID objectId;

    private UUID tagId;

    public ObjectTagDto(UUID objectId, UUID tagId) {
        this.objectId = objectId;
        this.tagId = tagId;
    }
}
