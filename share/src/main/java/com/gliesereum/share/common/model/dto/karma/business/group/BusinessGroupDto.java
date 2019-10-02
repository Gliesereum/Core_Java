package com.gliesereum.share.common.model.dto.karma.business.group;


import com.gliesereum.share.common.model.dto.karma.business.group.enumerated.BusinessGroupBy;
import com.gliesereum.share.common.model.dto.karma.business.document.BusinessDocumentDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class BusinessGroupDto {
    
    private Map<BusinessGroupBy, List<BusinessGroupListItemDto>> groups;
    
    private List<BusinessDocumentDto> business;
    
    private Map<String, BusinessGroupTagDto> tagGroups;
}
