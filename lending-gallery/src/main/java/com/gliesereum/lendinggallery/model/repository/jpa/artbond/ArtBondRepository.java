package com.gliesereum.lendinggallery.model.repository.jpa.artbond;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SpecialStatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.repository.AuditableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ArtBondRepository extends AuditableRepository<ArtBondEntity>, CustomizedArtBondRepository {

    List<ArtBondEntity> findAllByStatusTypeAndSpecialStatusTypeAndObjectState(StatusType statusType, SpecialStatusType specialStatusType, ObjectState objectState);

    List<ArtBondEntity> findAllByTagsContainsAndObjectState(List<String> tags, ObjectState objectState);
}
