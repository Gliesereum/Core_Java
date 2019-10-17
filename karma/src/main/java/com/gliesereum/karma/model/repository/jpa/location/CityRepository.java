package com.gliesereum.karma.model.repository.jpa.location;

import com.gliesereum.karma.model.entity.location.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CityRepository extends JpaRepository<CityEntity, UUID> {
}