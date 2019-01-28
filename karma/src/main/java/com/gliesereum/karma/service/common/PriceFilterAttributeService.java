package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.PriceFilterAttributeEntity;
import com.gliesereum.share.common.model.dto.karma.common.PriceFilterAttributeDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PriceFilterAttributeService extends DefaultService<PriceFilterAttributeDto, PriceFilterAttributeEntity> {

    void deleteByPriceIdAndFilterId(UUID idPrice, UUID filterAttributeId);

    boolean existByPriceIdAndAttributeId(UUID idPrice, UUID filterAttributeId);

}
