package com.gliesereum.karma.service.car;

import com.gliesereum.karma.model.entity.car.ModelCarEntity;
import com.gliesereum.share.common.model.dto.karma.car.ModelCarDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ModelCarService extends DefaultService<ModelCarDto, ModelCarEntity> {

    List<ModelCarDto> getAllByBrandId(UUID id);
}
