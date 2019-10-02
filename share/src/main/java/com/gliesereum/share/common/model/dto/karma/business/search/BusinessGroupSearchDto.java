package com.gliesereum.share.common.model.dto.karma.business.search;

import com.gliesereum.share.common.model.dto.karma.business.BusinessSearchDto;
import com.gliesereum.share.common.model.dto.karma.business.group.enumerated.BusinessGroupBy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessGroupSearchDto extends BusinessSearchDto {
    
    private Long countInGroups;
    
    private List<BusinessGroupBy> groups;
    
    private List<String> tagGroups;
}
