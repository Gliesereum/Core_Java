package com.gliesereum.karma.service.carwash.impl;

import com.gliesereum.karma.aspect.annotation.UpdateCarWashIndex;
import com.gliesereum.karma.model.entity.carwash.CarWashEntity;
import com.gliesereum.karma.model.repository.jpa.carwash.CarWashRepository;
import com.gliesereum.karma.service.carwash.CarWashRecordService;
import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.karma.service.comment.CommentService;
import com.gliesereum.karma.service.common.PackageService;
import com.gliesereum.karma.service.common.ServicePriceService;
import com.gliesereum.karma.service.media.MediaService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashFullModel;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.model.dto.karma.common.PackageDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.common.WorkTimeDto;
import com.gliesereum.share.common.model.dto.karma.common.WorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_IN_BAN;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_AUTHENTICATION;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Slf4j
@Service
public class CarWashServiceImpl extends DefaultServiceImpl<CarWashDto, CarWashEntity> implements CarWashService {

    private static final Class<CarWashDto> DTO_CLASS = CarWashDto.class;
    private static final Class<CarWashEntity> ENTITY_CLASS = CarWashEntity.class;

    private final CarWashRepository carWashRepository;

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CarWashRecordService carWashRecordService;

    public CarWashServiceImpl(CarWashRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.carWashRepository = repository;
    }

    @Override
    @UpdateCarWashIndex
    public CarWashDto create(CarWashDto dto) {
        checkUserByStatus();
        if (dto != null) {
            UUID userBusinessId = SecurityUtil.getUserBusinessId();
            if (userBusinessId == null) {
                throw new ClientException(DONT_HAVE_PERMISSION_TO_CREATE_CARWASH);
            }
            dto.setUserBusinessId(userBusinessId);
            CarWashEntity entity = converter.convert(dto, entityClass);
            entity = repository.saveAndFlush(entity);
            dto = converter.convert(entity, dtoClass);
        }
        return dto;
    }

    @Override
    @UpdateCarWashIndex
    public CarWashDto update(CarWashDto dto) {
        checkUserByStatus();
        if (dto != null) {
            if (dto.getId() == null) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            currentUserHavePermissionToAction(dto.getId());
            dto.setUserBusinessId(SecurityUtil.getUserBusinessId());
            CarWashEntity entity = converter.convert(dto, entityClass);
            entity = repository.saveAndFlush(entity);
            dto = converter.convert(entity, dtoClass);
        }
        return dto;
    }

    @Override
    public boolean existByIdAndUserBusinessId(UUID id, UUID userBusinessId) {
        return carWashRepository.existsByIdAndUserBusinessId(id, userBusinessId);
    }

    @Override
    public boolean currentUserHavePermissionToAction(UUID carWashId) {
        boolean result = false;
        UUID userBusinessId = SecurityUtil.getUserBusinessId();
        if (userBusinessId != null) {
            result = existByIdAndUserBusinessId(carWashId, userBusinessId);
        }
        return result;
    }

    @Override
    public List<CarWashDto> getByUserBusinessId(UUID userBusinessId) {
        List<CarWashDto> result = null;
        if (userBusinessId != null) {
            List<CarWashEntity> entities = carWashRepository.findByUserBusinessId(userBusinessId);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }


    private void checkUserByStatus() {
        if (SecurityUtil.getUser() == null) {
            throw new ClientException(USER_NOT_AUTHENTICATION);
        }
        UserDto user = SecurityUtil.getUser().getUser();
        if (user.getBanStatus().equals(BanStatus.BAN)) {
            throw new ClientException(USER_IN_BAN);
        }
    }

    @Override
    public CarWashFullModel getFullModelById(UUID id) {
        CarWashFullModel result = new CarWashFullModel();
        if (id == null) {
            throw new ClientException(CARWASH_ID_EMPTY);
        }
        CarWashDto carWashDto = getById(id);
        if (carWashDto == null) {
            throw new ClientException(CARWASH_NOT_FOUND);
        }
        result.setCarWashId(id);
        result.setLogoUrl(carWashDto.getLogoUrl());
        result.setName(carWashDto.getName());
        result.setDescription(carWashDto.getDescription());
        result.setAddress(carWashDto.getAddress());
        result.setPhone(carWashDto.getPhone());
        result.setAddPhone(carWashDto.getAddPhone());
        result.setLatitude(carWashDto.getLatitude());
        result.setLongitude(carWashDto.getLongitude());
        result.setRating(commentService.getRating(id));

        if (CollectionUtils.isNotEmpty(carWashDto.getWorkTimes())) {
            result.setWorkTimes(carWashDto.getWorkTimes());
        } else {
            List<WorkTimeDto> emptyList = Collections.emptyList();
            result.setWorkTimes(emptyList);
        }

        if (CollectionUtils.isNotEmpty(carWashDto.getSpaces())) {
            result.setSpaces(carWashDto.getSpaces());
        } else {
            List<WorkingSpaceDto> emptyList = Collections.emptyList();
            result.setSpaces(emptyList);
        }

        List<ServicePriceDto> servicePrices = servicePriceService.getByBusinessServiceId(id);
        if (CollectionUtils.isNotEmpty(servicePrices)) {
            result.setServicePrices(servicePrices);
        } else {
            List<ServicePriceDto> emptyList = Collections.emptyList();
            result.setServicePrices(emptyList);
        }
        List<PackageDto> packages = packageService.getByBusinessServiceId(id);
        if (CollectionUtils.isNotEmpty(packages)) {
            result.setPackages(packages);
        } else {
            List<PackageDto> emptyList = Collections.emptyList();
            result.setPackages(emptyList);
        }
        List<MediaDto> media = mediaService.getByObjectId(id);
        if (CollectionUtils.isNotEmpty(media)) {
            result.setMedia(media);
        } else {
            List<MediaDto> emptyList = Collections.emptyList();
            result.setMedia(emptyList);
        }
        List<CommentDto> comments = commentService.findByObjectId(id);
        if (CollectionUtils.isNotEmpty(comments)) {
            result.setComments(comments);
        } else {
            List<CommentDto> emptyList = Collections.emptyList();
            result.setComments(emptyList);
        }
        List<CarWashRecordDto> records = carWashRecordService.getByIdCarWashAndStatusRecord(id, StatusRecord.CREATED,
                LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX));
        if (CollectionUtils.isNotEmpty(records)) {
            result.setRecords(records);
        } else {
            List<CarWashRecordDto> emptyList = Collections.emptyList();
            result.setRecords(emptyList);
        }
        return result;
    }
}
