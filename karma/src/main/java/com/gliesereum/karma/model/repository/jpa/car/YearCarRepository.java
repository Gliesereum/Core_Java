package com.gliesereum.karma.model.repository.jpa.car;

import com.gliesereum.karma.model.entity.car.YearCarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface YearCarRepository extends JpaRepository<YearCarEntity, UUID> {
}
