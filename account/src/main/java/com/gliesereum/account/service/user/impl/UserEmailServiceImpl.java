package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserEmailEntity;
import com.gliesereum.account.model.repository.jpa.user.UserEmailRepository;
import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.user.UserEmailDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

import static com.gliesereum.share.common.exception.messages.EmailExceptionMessage.*;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@Service
public class UserEmailServiceImpl extends DefaultServiceImpl<UserEmailDto, UserEmailEntity> implements UserEmailService {

    @Autowired
    private UserEmailRepository repository;

    private static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    private static final Class<UserEmailDto> DTO_CLASS = UserEmailDto.class;
    private static final Class<UserEmailEntity> ENTITY_CLASS = UserEmailEntity.class;

    public UserEmailServiceImpl(UserEmailRepository userEmailRepository, DefaultConverter defaultConverter) {
        super(userEmailRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }


    @Override
    public void deleteByUserId(UUID id) {
        if(id != null){
          repository.deleteUserEmailEntityByUserId(id);
        }
    }

    public void checkIsEmail(String str) {
        if (!emailPattern.matcher(str).matches()) {
            throw new ClientException(NOT_EMAIL_BY_REGEX);
        }
    }
}
