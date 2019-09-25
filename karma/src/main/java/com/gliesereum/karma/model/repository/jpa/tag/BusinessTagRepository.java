package com.gliesereum.karma.model.repository.jpa.tag;

import com.gliesereum.karma.model.entity.tag.BusinessTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BusinessTagRepository extends JpaRepository<BusinessTagEntity, UUID> {

    BusinessTagEntity getByBusinessIdAndAndTagId(UUID businessId, UUID tagId);

    List<BusinessTagEntity> getByBusinessId(UUID businessId);

    List<BusinessTagEntity> getByBusinessIdIn(List<UUID> businessIds);

    void deleteAllByBusinessId(UUID businessId);
}