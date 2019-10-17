package com.gliesereum.karma.model.repository.jpa.location;

import com.gliesereum.karma.model.entity.location.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {
}