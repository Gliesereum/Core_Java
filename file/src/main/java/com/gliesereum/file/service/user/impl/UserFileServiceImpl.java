package com.gliesereum.file.service.user.impl;

import com.gliesereum.file.model.entity.UserFileEntity;
import com.gliesereum.file.model.repository.jpa.UserFileRepository;
import com.gliesereum.file.service.file.FileService;
import com.gliesereum.file.service.user.UserFileService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.file.UserFileDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.BODY_INVALID;
import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.MediaExceptionMessage.CURRENT_USER_DONT_HAVE_ACCESS_TO_FILE;
import static com.gliesereum.share.common.exception.messages.MediaExceptionMessage.USER_FILE_NOT_FOUND;

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
    private FileService fileService;

    @Autowired
    public UserFileServiceImpl(UserFileRepository userFileRepository, DefaultConverter defaultConverter) {
        super(userFileRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.userFileRepository = userFileRepository;
    }

    @Override
    public List<UserFileDto> getByUserId(UUID userId) {
        List<UserFileDto> result = null;
        if (userId != null) {
            List<UserFileEntity> entities = userFileRepository.findAllByUserId(userId);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<UserFileDto> getAllCanRead(UUID userId) {
        List<UserFileDto> result = null;
        if (userId != null) {
            List<UserFileEntity> entities = userFileRepository.findAllByUserIdOrReaderIdsContains(userId, Arrays.asList(userId));
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public UserFileDto updateCurrentUser(UserFileDto userFileDto) {
        if (userFileDto == null) {
            throw new ClientException(BODY_INVALID);
        }
        UUID id = userFileDto.getId();
        UserFileDto existed = getFileAndCheckPermission(id);
        existed.setCrypto(userFileDto.getCrypto());
        existed.setKeys(userFileDto.getKeys());
        existed.setReaderIds(userFileDto.getReaderIds());
        if (!userFileDto.getOpen().equals(existed.getOpen())) {
            existed.setOpen(userFileDto.getOpen());
            fileService.changeAccessToFile(existed);
        }
        return super.update(existed);
    }

    @Override
    public void deleteCurrentUser(UUID fileId) {
        UserFileDto existed = getFileAndCheckPermission(fileId);
        fileService.delete(existed.getFilename());
        delete(fileId);
    }

    public UserFileDto getFileAndCheckPermission(UUID id) {
        if (id == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        UserFileDto existed = super.getById(id);
        if (existed == null) {
            throw new ClientException(USER_FILE_NOT_FOUND);
        }
        if (!existed.getUserId().equals(SecurityUtil.getUserId())) {
            throw new ClientException(CURRENT_USER_DONT_HAVE_ACCESS_TO_FILE);
        }
        return existed;
    }
}
