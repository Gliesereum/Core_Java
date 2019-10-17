package com.gliesereum.lendinggallery.model.repository.jpa.artbond;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtBondTokenRepository extends JpaRepository<ArtBondTokenEntity, UUID> {
}