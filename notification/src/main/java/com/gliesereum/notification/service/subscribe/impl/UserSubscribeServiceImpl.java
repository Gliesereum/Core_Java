package com.gliesereum.notification.service.subscribe.impl;

import com.gliesereum.notification.model.entity.subscribe.UserSubscribeEntity;
import com.gliesereum.notification.model.repository.jpa.subscribe.UserSubscribeRepository;
import com.gliesereum.notification.service.subscribe.UserSubscribeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.notification.subscribe.UserSubscribeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class UserSubscribeServiceImpl
		extends DefaultServiceImpl<UserSubscribeDto, UserSubscribeEntity>
		implements UserSubscribeService {

	private static final Class<UserSubscribeDto> DTO_CLASS = UserSubscribeDto.class;

	private static final Class<UserSubscribeEntity> ENTITY_CLASS = UserSubscribeEntity.class;

	private final UserSubscribeRepository userSubscribeRepository;

	@Autowired
	public UserSubscribeServiceImpl(UserSubscribeRepository userSubscribeRepository,
			DefaultConverter defaultConverter) {
		super(userSubscribeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
		this.userSubscribeRepository = userSubscribeRepository;
	}

}