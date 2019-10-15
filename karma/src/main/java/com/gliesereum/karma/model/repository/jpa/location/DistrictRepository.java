package com.gliesereum.karma.model.repository.jpa.location;

import com.gliesereum.karma.model.entity.location.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DistrictRepository extends JpaRepository<DistrictEntity, UUID> {

    List<DistrictEntity> getAllByCityId(UUID id);
}