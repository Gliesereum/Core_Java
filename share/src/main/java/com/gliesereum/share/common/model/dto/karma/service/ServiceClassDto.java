package com.gliesereum.share.common.model.dto.karma.service;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceClassDto extends DefaultDto {

    @NotEmpty
    private String name;

    private String description;

    private Integer orderIndex;

    private UUID businessCategoryId;
}
