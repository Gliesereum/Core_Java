package com.gliesereum.karma.service.popular;

import com.gliesereum.karma.model.entity.popular.BusinessPopularEntity;
import com.gliesereum.share.common.model.dto.karma.business.popular.BusinessPopularDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;


public interface BusinessPopularService extends DefaultService<BusinessPopularDto, BusinessPopularEntity> {
    
    void updateBusinessCountAsync(UUID businessId);
    
    void updateBusinessCountAsync(List<UUID> businessIds);
    
    List<BusinessPopularDto> getByBusinessIds(Iterable<UUID> businessIds);
    
    List<BusinessPopularDto> getByBusinessIds(Iterable<UUID> businessIds, int size);
}
