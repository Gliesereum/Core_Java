package com.gliesereum.lendinggallery.model.repository.jpa.artbond;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SpecialStatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ArtBondRepository extends JpaRepository<ArtBondEntity, UUID>, CustomizedArtBondRepository {

    List<ArtBondEntity> findAllByStatusTypeAndSpecialStatusType(StatusType statusType, SpecialStatusType specialStatusType);

    List<ArtBondEntity> findAllByTagsContains(List<String> tags);
}
