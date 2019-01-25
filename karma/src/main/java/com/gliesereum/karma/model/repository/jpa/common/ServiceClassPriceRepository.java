package com.gliesereum.karma.model.repository.jpa.common;

import com.gliesereum.karma.model.entity.common.ServiceClassPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ServiceClassPriceRepository extends JpaRepository<ServiceClassPriceEntity, UUID> {

    List<ServiceClassPriceEntity> findAllByPriceId(UUID priceId);

    Optional<ServiceClassPriceEntity> findByPriceIdAndServiceClassId(UUID priceId, UUID serviceClassId);
}
