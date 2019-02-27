package com.gliesereum.lendinggallery.service.customer;

import com.gliesereum.lendinggallery.model.entity.customer.CustomerEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CustomerService extends DefaultService<CustomerDto, CustomerEntity> {

    CustomerDto findByUserId(UUID id);
}
