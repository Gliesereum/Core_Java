package com.gliesereum.lendinggallery.model.repository.jpa.offer;

import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import com.gliesereum.share.common.model.query.lendinggallery.offer.OperationsStoryQuery;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CustomizedOperationsStoryRepository {

    List<OperationsStoryEntity> search(OperationsStoryQuery query, UUID customerId);
}
