package com.gliesereum.share.common.model.dto.karma.comment;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentFullDto extends DefaultDto {

    private UUID objectId;

    private String text;

    private Integer rating;

    private UUID ownerId;

    private String firstName;

    private String lastName;

    private String middleName;
}
