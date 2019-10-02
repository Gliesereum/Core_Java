package com.gliesereum.share.common.model.dto.karma.business.group;

import com.gliesereum.share.common.model.dto.karma.business.document.BusinessDocumentDto;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BusinessGroupTagDto {
    
    private TagDto tag;
    
    private List<BusinessDocumentDto> business;
}
