package com.gliesereum.lendinggallery.model.repository.jpa.customer;

import com.gliesereum.lendinggallery.model.entity.customer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    CustomerEntity findByUserId(UUID id);
}
