package com.gliesereum.lendinggallery.service.account;

import com.gliesereum.lendinggallery.model.entity.account.MessageEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.account.MessageDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SectionType;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
public interface MessageService extends DefaultService<MessageDto, MessageEntity> {
   
    List<MessageDto> getAllByUser();

    List<MessageDto> getAllBySections(List<SectionType> types);
}    