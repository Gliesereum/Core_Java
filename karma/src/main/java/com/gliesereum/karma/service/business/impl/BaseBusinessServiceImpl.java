package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.model.entity.business.BaseBusinessEntity;
import com.gliesereum.karma.model.repository.jpa.business.BaseBusinessRepository;
import com.gliesereum.karma.service.administrator.BusinessAdministratorService;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.business.descriptions.BusinessDescriptionService;
import com.gliesereum.karma.service.comment.CommentService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.media.MediaService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.karma.service.service.PackageService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.administrator.BusinessAdministratorDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessFullModel;
import com.gliesereum.share.common.model.dto.karma.business.LiteBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.descriptions.BusinessDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentFullDto;
import com.gliesereum.share.common.model.dto.karma.comment.RatingDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${image-url.business.logo}")
    private String defaultBusinessLogo;

    @Value("${image-url.business.cover}")
    private String defaultBusinessCover;

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
    private BusinessDescriptionService businessDescriptionService;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @Autowired
    private BusinessAdministratorService businessAdministratorService;

    @Autowired
    public BaseBusinessServiceImpl(BaseBusinessRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.baseBusinessRepository = repository;
    }

    @Override
    public BaseBusinessDto create(BaseBusinessDto dto) {
        SecurityUtil.checkUserByBanStatus();
        if (dto != null) {
            setLogoIfNull(dto);
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
            setLogoIfNull(dto);
            checkBusinessCategory(dto);
            if (dto.getId() == null) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            businessPermissionFacade.checkPermissionByBusiness(dto.getId(), BusinessPermission.BUSINESS_ADMINISTRATION);
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
    public BaseBusinessDto getByIdAndLock(UUID businessId) {
        BaseBusinessDto result = null;
        if (businessId != null) {
            BaseBusinessEntity entity = baseBusinessRepository.findByIdAndLock(businessId);
            result = converter.convert(entity, dtoClass);
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
    @Transactional
    public List<BaseBusinessDto> getAllBusinessByCurrentUser() {
        List<BaseBusinessDto> result = new ArrayList<>();
        List<UUID> businessIds = getAllBusinessIdsByCurrentUser();
        if (CollectionUtils.isNotEmpty(businessIds)) {
            result = getByIds(businessIds);
        }
        return result;
    }

    @Override
    public List<UUID> getAllBusinessIdsByCurrentUser() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_NOT_AUTHENTICATION);
        }
        Set<UUID> businessIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(SecurityUtil.getUserCorporationIds())) {
            List<UUID> businessByCorporation = baseBusinessRepository.getIdsByCorporationIdIn(SecurityUtil.getUserCorporationIds());
            if (CollectionUtils.isNotEmpty(businessByCorporation)) {
                businessIds.addAll(businessByCorporation);
            }
        }
        List<WorkerDto> workers = workerService.findByUserId(SecurityUtil.getUserId());
        if (CollectionUtils.isNotEmpty(workers)) {
            businessIds.addAll(workers.stream().map(WorkerDto::getBusinessId).collect(Collectors.toList()));
        }
        List<BusinessAdministratorDto> administrator = businessAdministratorService.getByUserId(SecurityUtil.getUserId());
        if (CollectionUtils.isNotEmpty(administrator)) {
            businessIds.addAll(administrator.stream().map(BusinessAdministratorDto::getBusinessId).collect(Collectors.toList()));
        }
        return new ArrayList<>(businessIds);
    }

    @Override
    @Transactional
    public List<BusinessFullModel> getAllFullBusinessByCurrentUser() {
        List<BusinessFullModel> result = new ArrayList<>();
        List<UUID> businessIds = getAllBusinessIdsByCurrentUser();
        if (CollectionUtils.isNotEmpty(businessIds)) {
            result = getFullModelByIds(businessIds);
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
        Map<UUID, List<WorkerDto>> workers = workerService.getWorkerMapByBusinessIds(ids);
        Map<UUID, RatingDto> ratings = commentService.getRatings(ids);
        Map<UUID, List<ServicePriceDto>> servicePrices = servicePriceService.getMapByBusinessIds(ids);
        Map<UUID, List<PackageDto>> packages = packageService.getMapByBusinessIds(ids);
        Map<UUID, List<MediaDto>> medias = mediaService.getMapByObjectIds(ids);
        Map<UUID, List<CommentFullDto>> comments = commentService.getMapFullByObjectIds(ids);

        return entities.stream()
                .map(i -> converter.convert(i, BusinessFullModel.class))
                .peek(i -> {
                    UUID id = i.getId();
                    i.setRating(ratings.get(id));
                    i.setBusinessId(id);
                    i.setServicePrices(ListUtils.emptyIfNull(servicePrices.get(id)));
                    i.setPackages(ListUtils.emptyIfNull(packages.get(id)));
                    i.setMedia(ListUtils.emptyIfNull(medias.get(id)));
                    i.setComments(ListUtils.emptyIfNull(comments.get(id)));
                    i.setWorkers(ListUtils.emptyIfNull(workers.get(id)));
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        businessPermissionFacade.checkPermissionByBusiness(id, BusinessPermission.BUSINESS_ADMINISTRATION);
        BaseBusinessDto dto = getById(id);
        if (dto == null) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        dto.setObjectState(ObjectState.DELETED);
        super.update(dto);
        businessEsService.indexAsync(dto.getId());
    }

    @Override
    public Map<UUID, LiteBusinessDto> getLiteBusinessMapByIds(Collection<UUID> collect) {
        Map<UUID, LiteBusinessDto> result = new HashMap<>();
        List<LiteBusinessDto> list = getLiteBusinessByIds(new ArrayList<>(collect));
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.stream().collect(Collectors.toMap(LiteBusinessDto::getId, i -> i));
        }
        return result;
    }

    @Override
    public List<LiteBusinessDto> getLiteBusinessByIds(Collection<UUID> ids) {
        List<LiteBusinessDto> result = null;
        if (CollectionUtils.isNotEmpty(ids)) {
            List<BaseBusinessEntity> entities = repository.findAllById(ids);
            result = converter.convert(entities, LiteBusinessDto.class);
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

    @Override
    public CommentDto addComment(UUID objectId, UUID userId, CommentDto comment) {
        CommentDto result = commentService.addComment(objectId, userId, comment);
        if (result != null) {
            businessEsService.indexAsync(result.getObjectId());
        }
        return result;
    }

    @Override
    public CommentDto updateComment(UUID userId, CommentDto comment) {
        CommentDto result = commentService.updateComment(userId, comment);
        if (result != null) {
            businessEsService.indexAsync(result.getObjectId());
        }
        return result;
    }

    @Override
    public void deleteComment(UUID commentId, UUID userId) {
        CommentDto deletedComment = commentService.deleteComment(commentId, userId);
        if (deletedComment != null) {
            businessEsService.indexAsync(deletedComment.getObjectId());
        }
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

    private void setLogoIfNull(BaseBusinessDto business) {
        if (business != null) {
            if (StringUtils.isBlank(business.getLogoUrl())) {
                business.setLogoUrl(defaultBusinessLogo);
            }
            if (StringUtils.isBlank(business.getCoverUrl())) {
                business.setCoverUrl(defaultBusinessCover);
            }
        }
    }
}
