package com.gliesereum.share.common.service.descrption;

import com.gliesereum.share.common.model.dto.base.description.BaseDescriptionDto;
import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
import com.gliesereum.share.common.model.entity.description.BaseDescriptionEntity;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface DefaultDescriptionService<D extends BaseDescriptionDto, E extends BaseDescriptionEntity> extends DefaultService<D, E> {

    DescriptionReadableDto<D> create(DescriptionReadableDto<D> description, UUID objectId);

    DescriptionReadableDto<D> update(DescriptionReadableDto<D> description, UUID objectId);

    void deleteByObjectId(UUID objectId);
}