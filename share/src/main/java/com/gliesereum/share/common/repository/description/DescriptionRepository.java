package com.gliesereum.share.common.repository.description;

import com.gliesereum.share.common.model.entity.description.BaseDescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@NoRepositoryBean
public interface DescriptionRepository<T extends BaseDescriptionEntity> extends JpaRepository<T, UUID> {

    void deleteAllByObjectId(UUID objectId);
}
