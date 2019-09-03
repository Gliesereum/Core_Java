package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.CorporationEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.repository.AuditableRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */

public interface CorporationRepository extends AuditableRepository<CorporationEntity> {

    List<CorporationEntity> findAllByIdInAndObjectState(List<UUID> ids, ObjectState state);

    List<CorporationEntity> findAllByIdInAndObjectStateOrderByCreateDateDesc(List<UUID> ids, ObjectState state);

    List<CorporationEntity> findAllByObjectStateOrderByName(ObjectState state);

}
