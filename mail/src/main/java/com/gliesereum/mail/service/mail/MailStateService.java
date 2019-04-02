package com.gliesereum.mail.service.mail;

import com.gliesereum.mail.model.entity.MailStateEntity;
import com.gliesereum.share.common.model.dto.mail.MailStateDto;
import com.gliesereum.share.common.service.DefaultService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
public interface MailStateService extends DefaultService<MailStateDto, MailStateEntity> {

    List<MailStateDto> getByMessageStatusAndDateAfter(Integer status, LocalDateTime date);

}    