package com.gliesereum.karma.facade.business;

import com.gliesereum.share.common.model.dto.karma.business.group.BusinessGroupDto;
import com.gliesereum.share.common.model.dto.karma.business.search.BusinessGroupSearchDto;

public interface BusinessSearchFacade {
    
    BusinessGroupDto getBusinessGroup(BusinessGroupSearchDto groupSearch);
}
