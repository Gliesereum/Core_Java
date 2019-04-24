package com.gliesereum.share.common.model.dto.karma.filter;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FilterDto extends DefaultDto {

    @NotEmpty
    private String value;

    @NotEmpty
    private String title;

    //TODO: remove
    //private ServiceType serviceType;

    private UUID businessCategoryId;

    private List<FilterAttributeDto> attributes = new ArrayList<>();
}
