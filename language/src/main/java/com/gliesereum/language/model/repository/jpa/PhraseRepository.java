package com.gliesereum.language.model.repository.jpa;

import com.gliesereum.language.model.entity.PhraseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface PhraseRepository extends JpaRepository<PhraseEntity, UUID> {

    void deleteAllByPackageId(UUID packageId);
}
