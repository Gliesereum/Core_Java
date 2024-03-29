package com.gliesereum.karma.service.car;

import com.gliesereum.karma.model.entity.car.BrandCarEntity;
import com.gliesereum.share.common.model.dto.karma.car.BrandCarDto;
import com.gliesereum.share.common.service.DefaultService;

/**
 * @author vitalij
 * @version 1.0
 */
public interface BrandCarService extends DefaultService<BrandCarDto, BrandCarEntity> {

    BrandCarDto getByName(String name);

    BrandCarDto getByNameOrCreate(String name);
}
