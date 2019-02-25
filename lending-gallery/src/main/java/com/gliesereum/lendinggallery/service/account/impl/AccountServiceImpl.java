package com.gliesereum.lendinggallery.service.account.impl;

import com.gliesereum.lendinggallery.model.entity.account.AccountEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.account.AccountRepository;
import com.gliesereum.lendinggallery.service.account.AccountService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.lendinggallery.account.AccountDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class AccountServiceImpl extends DefaultServiceImpl<AccountDto, AccountEntity> implements AccountService {

    private final AccountRepository repository;

    private static final Class<AccountDto> DTO_CLASS = AccountDto.class;
    private static final Class<AccountEntity> ENTITY_CLASS = AccountEntity.class;

    public AccountServiceImpl(AccountRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.repository = repository;
    }
}
