package com.gliesereum.lendinggallery.model.repository.jpa.offer;

import com.gliesereum.lendinggallery.model.entity.offer.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface DividendRepository extends JpaRepository<DividendEntity, UUID> {
}
