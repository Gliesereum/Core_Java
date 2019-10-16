package com.gliesereum.lendinggallery.model.repository.jpa.advisor;

import com.gliesereum.lendinggallery.model.entity.advisor.AdvisorEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.repository.AuditableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AdvisorRepository extends AuditableRepository<AdvisorEntity> {
	
	List<AdvisorEntity> findAllByUserIdAndObjectState(UUID userId, ObjectState objectState);
	
	boolean existsByUserIdAndArtBondIdAndObjectState(UUID userId, UUID artBondId, ObjectState objectState);
	
	Page<AdvisorEntity> findAllByArtBondIdAndObjectState(UUID artBondId, ObjectState objectState, Pageable pageable);
}
