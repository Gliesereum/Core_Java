package com.gliesereum.mail.service.feedback;

import com.gliesereum.mail.model.entity.FeedBackUserEntity;
import com.gliesereum.share.common.model.dto.mail.FeedBackUserDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface FeedBackUserService extends DefaultService<FeedBackUserDto, FeedBackUserEntity> {
    
    List<FeedBackUserDto> getAllByAppId(UUID id);

    List<FeedBackUserDto> createList(List<FeedBackUserDto> dtos);
}    