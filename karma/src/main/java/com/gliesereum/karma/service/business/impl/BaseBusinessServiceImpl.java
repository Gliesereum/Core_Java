package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.model.document.ClientDocument;
import com.gliesereum.karma.model.entity.business.BaseBusinessEntity;
import com.gliesereum.karma.model.repository.jpa.business.BaseBusinessRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.business.descriptions.BusinessDescriptionService;
import com.gliesereum.karma.service.comment.CommentService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.es.ClientEsService;
import com.gliesereum.karma.service.media.MediaService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.karma.service.service.PackageService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessFullModel;
import com.gliesereum.share.common.model.dto.karma.business.LiteBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.descriptions.BusinessDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordsSearchDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.CORPORATION_ID_IS_EMPTY;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_AUTHENTICATION;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class BaseBusinessServiceImpl extends DefaultServiceImpl<BaseBusinessDto, BaseBusinessEntity> implements BaseBusinessService {

    private static final Class<BaseBusinessDto> DTO_CLASS = BaseBusinessDto.class;
    private static final Class<BaseBusinessEntity> ENTITY_CLASS = BaseBusinessEntity.class;

    private final BaseBusinessRepository baseBusinessRepository;

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BaseRecordService baseRecordService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private BusinessEsService businessEsService;

    @Autowired
    private ClientEsService clientEsService;

    @Autowired
    private BusinessDescriptionService businessDescriptionService;

    @Autowired
    public BaseBusinessServiceImpl(BaseBusinessRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.baseBusinessRepository = repository;
    }

    @Override
    public BaseBusinessDto create(BaseBusinessDto dto) {
        SecurityUtil.checkUserByBanStatus();
        if (dto != null) {
            checkBusinessCategory(dto);
            checkCorporationId(dto);
            dto.setObjectState(ObjectState.ACTIVE);
            dto.setId(null);
            BaseBusinessEntity entity = converter.convert(dto, entityClass);
            entity = repository.saveAndFlush(entity);
            businessDescriptionService.create(dto.getDescriptions(), entity.getId());
            baseBusinessRepository.refresh(entity);
            dto = converter.convert(entity, dtoClass);
            businessEsService.indexAsync(dto.getId());
        }
        return dto;
    }

    @Override
    public BaseBusinessDto update(BaseBusinessDto dto) {
        SecurityUtil.checkUserByBanStatus();
        if (dto != null) {
            checkBusinessCategory(dto);
            if (dto.getId() == null) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            this.currentUserHavePermissionToActionInBusinessLikeOwner(dto.getId());
            checkCorporationId(dto);
            BaseBusinessEntity entity = converter.convert(dto, entityClass);
            entity = repository.saveAndFlush(entity);
            List<BusinessDescriptionDto> descriptions = businessDescriptionService.update(dto.getDescriptions(), entity.getId());
            dto = converter.convert(entity, dtoClass);
            dto.setDescriptions(descriptions);
            businessEsService.indexAsync(dto.getId());
        }
        return dto;
    }

    @Override
    @Transactional
    public List<BaseBusinessDto> getAll() {
        List<BaseBusinessEntity> entities = baseBusinessRepository.getAllByObjectState(ObjectState.ACTIVE);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<LiteBusinessDto> getAllLite() {
        List<BaseBusinessEntity> entities = baseBusinessRepository.getAllByObjectState(ObjectState.ACTIVE);
        return converter.convert(entities, LiteBusinessDto.class);
    }

    @Override
    public LiteBusinessDto getLiteById(UUID businessId) {
        LiteBusinessDto result = null;
        if (businessId != null) {
            BaseBusinessEntity entity = baseBusinessRepository.findByIdAndObjectState(businessId, ObjectState.ACTIVE);
            result = converter.convert(entity, LiteBusinessDto.class);
        }
        return result;
    }

    @Override
    public BaseBusinessDto getById(UUID id) {
        BaseBusinessEntity entity = baseBusinessRepository.findByIdAndObjectState(id, ObjectState.ACTIVE);
        return converter.convert(entity, dtoClass);
    }

    @Override
    public List<BaseBusinessDto> getAllIgnoreState() {
        return super.getAll();
    }

    @Override
    public BaseBusinessDto getByIdIgnoreState(UUID id) {
        return super.getById(id);
    }

    @Override
    public List<BaseBusinessDto> getAllBusinessByCurrentUser() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_NOT_AUTHENTICATION);
        }
        Set<BaseBusinessDto> set = new HashSet<>();
        if (CollectionUtils.isNotEmpty(SecurityUtil.getUserCorporationIds())) {
            List<BaseBusinessDto> byCorporationIds = getByCorporationIds(SecurityUtil.getUserCorporationIds());
            if (CollectionUtils.isNotEmpty(byCorporationIds)) {
                set.addAll(byCorporationIds);
            }
        }
        List<WorkerDto> workers = workerService.findByUserId(SecurityUtil.getUserId());
        if (CollectionUtils.isNotEmpty(workers)) {
            workers.forEach(f -> {
                BaseBusinessDto business = getById(f.getBusinessId());
                if (business != null) {
                    set.add(business);
                }
            });
        }
        return new ArrayList<>(set);
    }

    @Override
    public List<BusinessFullModel> getAllFullBusinessByCurrentUser() {
        List<BusinessFullModel> result = new ArrayList<>();
        List<BaseBusinessDto> list = getAllBusinessByCurrentUser();
        if (CollectionUtils.isNotEmpty(list)) {
            result = getFullModelByIds(list.stream().map(BaseBusinessDto::getId).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(result)) {
                result.forEach(i -> i.setRecords(ListUtils.emptyIfNull
                        (baseRecordService.getByBusinessIdAndStatusRecord(
                                i.getId(),
                                StatusRecord.CREATED,
                                LocalDate.now().atStartOfDay(),
                                LocalDate.now().atTime(LocalTime.MAX)))));
            }
        }
        return result;
    }

    @Override
    public void deleteByCorporationIdCheckPermission(UUID id) {
        List<BaseBusinessDto> list = getByCorporationId(id);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(m -> m.setObjectState(ObjectState.DELETED));
            super.update(list);
            businessEsService.indexAsync(list.stream().map(BaseBusinessDto::getId).collect(Collectors.toList()));
        }
    }

    @Override
    public void deleteByCorporationId(UUID id) {
        List<BaseBusinessEntity> entities = baseBusinessRepository.findByCorporationIdAndObjectState(id, ObjectState.ACTIVE);
        if (CollectionUtils.isNotEmpty(entities)) {
            entities.forEach(i -> i.setObjectState(ObjectState.DELETED));
            baseBusinessRepository.saveAll(entities);
            baseBusinessRepository.flush();
            businessEsService.indexAsync(entities.stream().map(BaseBusinessEntity::getId).collect(Collectors.toList()));
        }
    }

    @Override
    @Transactional
    public List<BaseBusinessDto> getByIds(Iterable<UUID> ids) {
        List<BaseBusinessDto> result = null;
        if (ids != null) {
            List<BaseBusinessEntity> entities = baseBusinessRepository.getAllByIdInAndObjectState(ids, ObjectState.ACTIVE);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public boolean existByIdAndCorporationIds(UUID id, List<UUID> corporationIds) {
        return baseBusinessRepository.existsByIdAndCorporationIdInAndObjectState(id, corporationIds, ObjectState.ACTIVE);
    }

    @Override
    public boolean currentUserHavePermissionToActionInBusinessLikeOwner(UUID businessId) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_NOT_AUTHENTICATION);
        }
        boolean result = false;
        List<UUID> userCorporationIds = SecurityUtil.getUserCorporationIds();
        if (CollectionUtils.isNotEmpty(userCorporationIds)) {
            result = existByIdAndCorporationIds(businessId, userCorporationIds);
        }
        return result;
    }

    @Override
    public boolean currentUserHavePermissionToActionInBusinessLikeWorker(UUID businessId) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_NOT_AUTHENTICATION);
        }
        if (businessId == null) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        return workerService.findByUserIdAndBusinessId(SecurityUtil.getUserId(), businessId) != null;
    }

    @Override
    public boolean currentUserHavePermissionToActionInCorporationLikeWorker(UUID corporationId) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_NOT_AUTHENTICATION);
        }
        if (corporationId == null) {
            throw new ClientException(CORPORATION_ID_IS_EMPTY);
        }
        return CollectionUtils.isNotEmpty(workerService.findByUserIdAndCorporationId(SecurityUtil.getUserId(), corporationId));
    }

    @Override
    public List<BaseBusinessDto> getByCorporationIds(List<UUID> corporationIds) {
        List<BaseBusinessDto> result = null;
        if (CollectionUtils.isNotEmpty(corporationIds)) {
            List<BaseBusinessEntity> entities = baseBusinessRepository.findByCorporationIdInAndObjectState(corporationIds, ObjectState.ACTIVE);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<BaseBusinessDto> getByCorporationId(UUID corporationId) {
        List<BaseBusinessDto> result = null;
        if (corporationId != null) {
            if (!SecurityUtil.userHavePermissionToCorporation(corporationId)) {
                throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
            }
            List<BaseBusinessEntity> entities = baseBusinessRepository.findByCorporationIdAndObjectState(corporationId, ObjectState.ACTIVE);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<BusinessFullModel> getFullModelByIds(List<UUID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        List<BaseBusinessEntity> entities = baseBusinessRepository.findByIdInAndObjectState(ids, ObjectState.ACTIVE);
        if (CollectionUtils.isEmpty(entities)) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        return entities.stream().map(i -> {
            BusinessFullModel result = converter.convert(i, BusinessFullModel.class);
            result.setRating(commentService.getRating(i.getId()));
            result.setBusinessId(i.getId());
            result.setServicePrices(ListUtils.emptyIfNull(servicePriceService.getByBusinessId(i.getId())));
            result.setPackages(ListUtils.emptyIfNull(packageService.getByBusinessId(i.getId())));
            result.setMedia(ListUtils.emptyIfNull(mediaService.getByObjectId(i.getId())));
            result.setComments(ListUtils.emptyIfNull(commentService.findFullByObjectId(i.getId())));
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        this.currentUserHavePermissionToActionInBusinessLikeOwner(id);
        BaseBusinessDto dto = getById(id);
        if (dto == null) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        dto.setObjectState(ObjectState.DELETED);
        super.update(dto);
        businessEsService.indexAsync(dto.getId());
    }

    @Override
    public List<UUID> searchClient(RecordsSearchDto search) {
        List<UUID> result = null;
        if (search != null) {
            List<BaseRecordDto> records = baseRecordService.getByParamsForBusiness(search);
            if (CollectionUtils.isNotEmpty(records)) {
                result = records.stream()
                        .filter(i -> i.getClientId() != null)
                        .map(BaseRecordDto::getClientId)
                        .distinct()
                        .collect(Collectors.toList());
            }
        }
        return result;
    }

    @Override
    public Page<ClientDocument> getCustomersByBusinessIds(List<UUID> ids, int page, int size) {
        Page<ClientDocument> result = null;
        if (size == 0) size = 100;
        if (CollectionUtils.isNotEmpty(ids)) {
            ids.forEach(f -> {
                if (!currentUserHavePermissionToActionInBusinessLikeWorker(f) &&
                        !currentUserHavePermissionToActionInBusinessLikeOwner(f)) {
                    throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
                }
            });
            result = clientEsService.getClientsByBusinessIds(ids, page, size);
        }
        return result;
    }

    @Override
    public Page<ClientDocument> getAllCustomersByCorporationIds(List<UUID> ids, int page, int size, String query) {
        Page<ClientDocument> result = null;
        if (size == 0) size = 100;
        if (CollectionUtils.isNotEmpty(ids)) {
            ids.forEach(f -> {
                if (!SecurityUtil.getUserCorporationIds().containsAll(ids) && true) { //todo set check worker by permission
                    throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
                }
            });
            result = clientEsService.getClientsByCorporationIdsAndAutocompleteQuery(query, ids, page, size);
        }
        return result;
    }

    @Override
    public List<UUID> getIdsByCorporationIds(List<UUID> corporationIds) {
        if (CollectionUtils.isEmpty(corporationIds)) {
            throw new ClientException(CORPORATION_ID_IS_EMPTY);
        }
        return baseBusinessRepository.getIdsByCorporationIdIn(corporationIds);
    }

    private void checkCorporationId(BaseBusinessDto business) {
        UUID corporationId = business.getCorporationId();
        if (corporationId == null) {
            throw new ClientException(CORPORATION_ID_REQUIRED_FOR_THIS_ACTION);
        }
        if (!SecurityUtil.userHavePermissionToCorporation(corporationId)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

    private void checkBusinessCategory(BaseBusinessDto dto) {
        if (dto.getBusinessCategoryId() == null) {
            throw new ClientException(BUSINESS_CATEGORY_ID_EMPTY);
        }
    }
}
