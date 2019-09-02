package com.gliesereum.karma.model.repository.jpa.business;

import com.gliesereum.karma.model.entity.business.BaseBusinessEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BusinessLockableRepository {

    BaseBusinessEntity findByIdAndObjectStateAndLock(UUID id, ObjectState objectState);
}
