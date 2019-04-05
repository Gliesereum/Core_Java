package com.gliesereum.account.service.kyc.impl;

import com.gliesereum.account.model.entity.kyc.KycRequestEntity;
import com.gliesereum.account.model.repository.jpa.kyc.KycRequestRepository;
import com.gliesereum.account.service.kyc.KycFieldService;
import com.gliesereum.account.service.kyc.KycRequestFieldService;
import com.gliesereum.account.service.kyc.KycRequestService;
import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.account.service.user.CorporationSharedOwnershipService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.KycFieldType;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import com.gliesereum.share.common.model.dto.account.enumerated.KycStatus;
import com.gliesereum.share.common.model.dto.account.kyc.*;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.RegexUtil;
import com.gliesereum.share.common.util.SecurityUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.KycExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class KycRequestServiceImpl extends DefaultServiceImpl<KycRequestDto, KycRequestEntity> implements KycRequestService {

    private static final Class<KycRequestDto> DTO_CLASS = KycRequestDto.class;
    private static final Class<KycRequestEntity> ENTITY_CLASS = KycRequestEntity.class;

    private final KycRequestRepository kycRequestRepository;

    @Autowired
    private KycFieldService kycFieldService;

    @Autowired
    private KycRequestFieldService kycRequestFieldService;

    @Autowired
    private CorporationService corporationService;

    @Autowired
    private CorporationSharedOwnershipService corporationSharedOwnershipService;

    @Autowired
    private UserService userService;

    @Autowired
    public KycRequestServiceImpl(KycRequestRepository kycRequestRepository, DefaultConverter defaultConverter) {
        super(kycRequestRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.kycRequestRepository = kycRequestRepository;
    }

    @Override
    public KycRequestDto createRequest(KycValuesRequestDto createRequest) {
        KycRequestDto result;
        validateModel(createRequest);

        UUID userId = SecurityUtil.getUserId();
        KycRequestType requestType = createRequest.getRequestType();
        UUID objectId = createRequest.getObjectId();

        if (requestType.equals(KycRequestType.INDIVIDUAL)) {
            if (!userId.equals(objectId)) {
                throw new ClientException(KYC_REQUEST_CANT_TO_ANOTHER_USER);
            }
        } else {
            corporationService.checkCurrentUserForPermissionActionThisCorporation(objectId);
        }
        if (openKycRequestExist(objectId, requestType)) {
            throw new ClientException(KYC_REQUEST_FOR_OBJECT_EXIST);
        }
        List<KycRequestFieldDto> kycRequestFields = validateAndCreateFields(createRequest, kycFieldService.getByType(requestType), null);

        result = new KycRequestDto();
        result.setKycRequestType(requestType);
        result.setObjectId(objectId);
        result.setKycStatus(KycStatus.KYC_REQUESTED);
        result = super.create(result);
        UUID requestId = result.getId();
        if (CollectionUtils.isNotEmpty(kycRequestFields)) {
            kycRequestFields.forEach(i -> i.setKycRequestId(requestId));
            kycRequestFields = kycRequestFieldService.create(kycRequestFields);
            result.setFields(kycRequestFields);
        }
        return result;
    }

    @Override
    public KycRequestDto updateRequest(KycValuesRequestDto updateRequest) {
        if (updateRequest == null) {
            throw new ClientException(BODY_INVALID);
        }
        UUID requestId = updateRequest.getId();
        if (requestId == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        KycRequestDto request = super.getById(requestId);
        if (request == null) {
            throw new ClientException(NOT_EXIST_BY_ID);
        }
        if (!request.getKycStatus().equals(KycStatus.KYC_SENT_TO_REWORK)) {
            throw new ClientException(KYC_REQUEST_ONLY_SENT_TO_REWORK_CAN_UPDATE);
        }
        List<KycRequestFieldDto> kycRequestFields = validateAndCreateFields(updateRequest, kycFieldService.getByType(request.getKycRequestType()), requestId);
        kycRequestFieldService.deleteAllByKycRequestId(requestId);
        kycRequestFields = kycRequestFieldService.create(kycRequestFields);
        request.setFields(kycRequestFields);
        return request;
    }

    @Override
    public boolean openKycRequestExist(UUID objectId, KycRequestType requestType) {
        boolean result = false;
        if(ObjectUtils.allNotNull(objectId, requestType)) {
            result = kycRequestRepository.existsByKycRequestTypeAndObjectIdAndKycStatusNot(requestType, objectId, KycStatus.KYC_REJECTED);
        }
        return result;
    }

    @Override
    public KycRequestDto updateStatus(UUID id, KycStatus newStatus, String comment) {
        KycRequestDto result = null;
        if (ObjectUtils.allNotNull(id, newStatus)) {
            KycRequestDto request = super.getById(id);
            if (request == null) {
                throw new ClientException(NOT_EXIST_BY_ID);
            }
            KycRequestType requestType = request.getKycRequestType();
            KycStatus existedStatus = request.getKycStatus();
            UUID objectId = request.getObjectId();
            if (existedStatus.equals(KycStatus.KYC_PASSED)) {
                throw new ClientException(KYC_REQUEST_PASSED_CANNOT_UPDATE);
            }
            if (existedStatus.equals(KycStatus.KYC_REJECTED)) {
                throw new ClientException(KYC_REQUEST_REJECTED_CANNOT_UPDATE);
            }
            if (newStatus.equals(KycStatus.KYC_PASSED)) {
                if (requestType.equals(KycRequestType.INDIVIDUAL)) {
                    userService.setKycApproved(objectId);
                }
                if (requestType.equals(KycRequestType.CORPORATION)) {
                    corporationService.setKycApproved(objectId);
                }
            }
            request.setKycStatus(newStatus);
            request.setComment(comment);
            result = super.update(request);
        }
        return result;
    }

    @Override
    public List<KycRequestDto> getAllByUserId(UUID userId) {
        List<KycRequestDto> result = null;
        if (userId != null) {
            List<UUID> corporationIds = corporationSharedOwnershipService.getAllCorporationIdByUserId(userId);
            List<UUID> objectIds = new ArrayList<>();
            objectIds.add(userId);
            if (CollectionUtils.isNotEmpty(corporationIds)) {
                objectIds.addAll(corporationIds);
            }
            List<KycRequestEntity> entities = kycRequestRepository.findAllByObjectIdIn(objectIds);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<KycRequestFullModelDto> getFullModelAll() {
        List<KycRequestEntity> entities = repository.findAll();
        List<KycRequestFullModelDto> requested = converter.convert(entities, KycRequestFullModelDto.class);
        if (CollectionUtils.isNotEmpty(requested)) {
            requested.forEach(i -> {
                if (i.getKycRequestType() != null) {
                    if (i.getKycRequestType().equals(KycRequestType.INDIVIDUAL)) {
                        i.setUser(userService.getById(i.getObjectId()));
                    }
                    if (i.getKycRequestType().equals(KycRequestType.CORPORATION)) {
                        i.setCorporation(corporationService.getById(i.getObjectId()));
                    }
                }
            });
        }
        return requested;
    }

    private List<KycRequestFieldDto> validateAndCreateFields(KycValuesRequestDto createRequest, List<KycFieldDto> requiredFields, UUID requestId) {
        List<KycRequestFieldDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(requiredFields)) {
            Map<UUID, String> values = createRequest.getValues();
            if (MapUtils.isEmpty(values)) {
                throw new ClientException(KYC_FIELDS_IS_MISSED);
            }
            for (KycFieldDto requiredField : requiredFields) {
                String value = values.get(requiredField.getId());
                if (StringUtils.isEmpty(value)) {
                    if (requiredField.getRequired()) {
                        throw new ClientException(KYC_REQUIRED_FIELD_IS_MISSED);
                    } else {
                        continue;
                    }
                }
                KycFieldType fieldType = requiredField.getFieldType();
                if ((fieldType != null) && (fieldType.equals(KycFieldType.FILE_ID))) {
                    if (!RegexUtil.isUUID(value)) {
                        throw new ClientException(KYC_FIELD_FILE_ID_IS_NOT_UUID);
                    }
                }
                KycRequestFieldDto kycRequestField = new KycRequestFieldDto();
                kycRequestField.setKycFieldId(requiredField.getId());
                kycRequestField.setValue(value);
                kycRequestField.setKycRequestId(requestId);
                result.add(kycRequestField);
            }
        }
        return result;
    }

    private void validateModel(KycValuesRequestDto createRequest) {
        if (createRequest == null) {
            throw new ClientException(BODY_INVALID);
        }
        if (createRequest.getRequestType() == null) {
            throw new ClientException(KYC_REQUEST_TYPE_MISSED);
        }
        if (createRequest.getObjectId() == null) {
            throw new ClientException(KYC_OBJECT_ID_MISSED);
        }
    }

}
