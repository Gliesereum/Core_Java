package com.gliesereum.lendinggallery.model.repository.jpa.dividend;

import com.gliesereum.lendinggallery.model.entity.dividend.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface DividendRepository extends JpaRepository<DividendEntity, UUID> {
}
