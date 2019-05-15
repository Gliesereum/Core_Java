package com.gliesereum.share.common.model.dto.karma.business;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusSpace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LiteWorkingSpaceDto extends DefaultDto {

    private String name;

    private String description;

    private Integer indexNumber;

    private UUID businessId;

    private StatusSpace statusSpace;

    private UUID businessCategoryId;

}
