package com.gliesereum.karma.model.repository.jpa.location;

import com.gliesereum.karma.model.entity.location.GeoPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GeoPositionRepository extends JpaRepository<GeoPositionEntity, UUID> {
}