package com.gliesereum.lendinggallery.service.advisor;

import com.gliesereum.lendinggallery.model.entity.advisor.AdvisorEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.advisor.AdvisorDto;
import com.gliesereum.share.common.service.auditable.AuditableService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface AdvisorService extends AuditableService<AdvisorDto, AdvisorEntity> {
	
	List<AdvisorDto> findByUserId(UUID userId);
	
	boolean currentUserIsAdvisor(UUID artBondId);
	
	void checkCurrentUserIsAdvisor(UUID artBondId);
	
	boolean existByUserIdAndArtBondId(UUID userId, UUID artBondId);
	
	AdvisorDto createWithUser(AdvisorDto advisor);
	
	Page<AdvisorDto> getByArtBondId(UUID artBondId, boolean setUsers, Integer page, Integer size);
	
	void setUsers(List<AdvisorDto> advisors);
	
	Boolean checkAdvisorExistByPhone(String phone);

	List<UUID> getArtBondIdsByUserId(UUID userId);
}
