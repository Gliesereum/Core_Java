package com.gliesereum.karma.model.repository.jpa.business;

import com.gliesereum.karma.model.entity.business.BusinessCategoryEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.BusinessType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface BusinessCategoryRepository extends JpaRepository<BusinessCategoryEntity, UUID> {

    List<BusinessCategoryEntity> findByBusinessType(BusinessType businessType);

    BusinessCategoryEntity findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, UUID id);

    List<BusinessCategoryEntity> findAllByOrderByOrderIndexAsc();
}