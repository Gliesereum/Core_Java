package com.gliesereum.karma.model.repository.jpa.popular;

import com.gliesereum.karma.model.entity.popular.BusinessPopularEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BusinessPopularRepository extends JpaRepository<BusinessPopularEntity, UUID> {
    
    BusinessPopularEntity findByBusinessId(UUID businessId);
    
    List<BusinessPopularEntity> findByBusinessIdInOrderByCountDesc(Iterable<UUID> businessIds);
}
