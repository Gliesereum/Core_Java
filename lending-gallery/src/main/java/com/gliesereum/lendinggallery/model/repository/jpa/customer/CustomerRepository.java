package com.gliesereum.lendinggallery.model.repository.jpa.customer;

import com.gliesereum.lendinggallery.model.entity.customer.CustomerEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    CustomerEntity findByUserId(UUID id);

    List<CustomerEntity> findAllByCustomerType(CustomerType customerType);

    List<CustomerEntity> findAllByCustomerTypeAndIdIn(CustomerType customerType, List<UUID> ids);

    List<CustomerEntity> findAllByUserIdIn(List<UUID> userIds);

    Page<CustomerEntity> findAllByCustomerType(CustomerType customerType, Pageable pageable);

    Page<CustomerEntity> findAllByCustomerTypeAndIdIn(CustomerType customerType, List<UUID> ids, Pageable pageable);
}
