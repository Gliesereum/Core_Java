package com.gliesereum.share.common.model.dto.karma.comment;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */

@Data
@NoArgsConstructor
public class CommentDto extends DefaultDto {

    private UUID objectId;

    private String text;

    private Integer rating;

    private UUID ownerId;
}
