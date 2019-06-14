package com.gliesereum.karma.model.repository.jpa.car;

import com.gliesereum.karma.model.entity.car.BrandCarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface BrandCarRepository extends JpaRepository<BrandCarEntity, UUID> {

    BrandCarEntity findByName(String name);
}
