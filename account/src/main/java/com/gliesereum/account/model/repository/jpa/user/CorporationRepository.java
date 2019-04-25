package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.CorporationEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
@Repository
public interface CorporationRepository extends JpaRepository<CorporationEntity, UUID> {

    CorporationEntity findByIdAndObjectState(UUID id, ObjectState state);

    List<CorporationEntity> findAllByIdInAndObjectState(List<UUID> ids, ObjectState state);

    List<CorporationEntity> findAllByObjectStateOrderByName(ObjectState state);

}
