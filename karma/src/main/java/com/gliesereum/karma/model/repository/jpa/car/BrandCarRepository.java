package com.gliesereum.karma.model.repository.jpa.car;

import com.gliesereum.karma.model.entity.car.BrandCarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Repository
public interface BrandCarRepository extends JpaRepository<BrandCarEntity, UUID> {
}
