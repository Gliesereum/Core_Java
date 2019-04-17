package com.gliesereum.notification.service.device.impl;

import com.gliesereum.notification.model.entity.device.UserDeviceEntity;
import com.gliesereum.notification.model.repository.jpa.device.UserDeviceRepository;
import com.gliesereum.notification.service.device.UserDeviceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceDto;
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
public class UserDeviceServiceImpl extends
		DefaultServiceImpl<UserDeviceDto, UserDeviceEntity> implements UserDeviceService {

	private static final Class<UserDeviceDto> DTO_CLASS = UserDeviceDto.class;

	private static final Class<UserDeviceEntity> ENTITY_CLASS = UserDeviceEntity.class;

	private final UserDeviceRepository userDeviceRepository;

	@Autowired
	public UserDeviceServiceImpl(UserDeviceRepository userDeviceRepository,
			DefaultConverter defaultConverter) {
		super(userDeviceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
		this.userDeviceRepository = userDeviceRepository;
	}

}