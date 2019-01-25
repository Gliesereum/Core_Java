package com.gliesereum.media.service.user.impl;

import com.gliesereum.media.model.entity.UserFileEntity;
import com.gliesereum.media.model.repository.jpa.UserFileRepository;
import com.gliesereum.media.service.user.UserFileService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.media.UserFileDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class UserFileServiceImpl extends DefaultServiceImpl<UserFileDto, UserFileEntity> implements UserFileService {

    private static final Class<UserFileDto> DTO_CLASS = UserFileDto.class;
    private static final Class<UserFileEntity> ENTITY_CLASS = UserFileEntity.class;

    private final UserFileRepository userFileRepository;

    @Autowired
    public UserFileServiceImpl(UserFileRepository userFileRepository, DefaultConverter defaultConverter) {
        super(userFileRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.userFileRepository = userFileRepository;
    }
}
