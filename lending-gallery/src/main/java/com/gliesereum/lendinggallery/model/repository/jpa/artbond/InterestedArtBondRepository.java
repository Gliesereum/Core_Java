package com.gliesereum.lendinggallery.model.repository.jpa.artbond;

import com.gliesereum.lendinggallery.model.entity.artbond.InterestedArtBondEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface InterestedArtBondRepository extends JpaRepository<InterestedArtBondEntity, UUID> {

    InterestedArtBondEntity findByArtBondIdAndCustomerId(UUID atrBondId, UUID customerId);

    List<InterestedArtBondEntity> findByArtBondId(UUID atrBondId);

}