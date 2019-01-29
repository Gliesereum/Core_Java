package com.gliesereum.karma.model.repository.jpa.filter;

import com.gliesereum.karma.model.entity.filter.PriceFilterAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PriceFilterAttributeRepository extends JpaRepository<PriceFilterAttributeEntity, UUID> {

    void deleteByPriceIdAndFilterAttributeId(UUID idPrice, UUID filterAttributeId);

    boolean existsByPriceIdAndFilterAttributeId(UUID idPrice, UUID filterAttributeId);
}
