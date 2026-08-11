package com.gliesereum.lendinggallery.model.repository.jpa.artbond;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SpecialStatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.repository.AuditableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ArtBondRepository extends AuditableRepository<ArtBondEntity>, CustomizedArtBondRepository {

    List<ArtBondEntity> findAllByStatusTypeAndSpecialStatusTypeAndObjectState(StatusType statusType, SpecialStatusType specialStatusType, ObjectState objectState);

    /**
     * Art bonds carrying any of the given tags. `tags` is an @ElementCollection,
     * so this joins the art_bond_tag table; Distinct keeps a bond from being
     * returned once per matching tag.
     */
    List<ArtBondEntity> findDistinctByTagsInAndObjectState(Collection<String> tags, ObjectState objectState);
}
