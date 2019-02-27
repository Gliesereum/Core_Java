package com.gliesereum.lendinggallery.service.customer.impl;

import com.gliesereum.lendinggallery.model.entity.customer.CustomerEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.customer.CustomerRepository;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class CustomerServiceImpl extends DefaultServiceImpl<CustomerDto, CustomerEntity> implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    private static final Class<CustomerDto> DTO_CLASS = CustomerDto.class;
    private static final Class<CustomerEntity> ENTITY_CLASS = CustomerEntity.class;

    public CustomerServiceImpl(CustomerRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public CustomerDto findByUserId(UUID id){
        if(id == null){
            throw new ClientException(ID_IS_EMPTY);
        }
        CustomerEntity entity = repository.findByUserId(id);
        if(entity == null){
            throw new ClientException(CUSTOMER_NOT_FOUND_BY_USER_ID);
        }
        return converter.convert(entity, dtoClass);
    }
}
