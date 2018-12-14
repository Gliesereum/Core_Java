package com.gliesereum.share.common.model.dto.karma.comment;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentDto extends DefaultDto {

    private UUID objectId;

    private String text;

    @Min(1)
    @Max(5)
    @NotNull
    private Integer rating;

    private UUID ownerId;
}
