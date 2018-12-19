package com.gliesereum.karma.model.repository.jpa.karma;

import com.gliesereum.karma.model.entity.karma.KarmaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */

public interface KarmaRepository extends JpaRepository<KarmaEntity, UUID> {

    KarmaEntity findByObjectId(UUID objectId);
}
