package com.gliesereum.lendinggallery.service.advisor.impl;

import com.gliesereum.lendinggallery.model.entity.advisor.AdvisorEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.advisor.AdvisorRepository;
import com.gliesereum.lendinggallery.service.advisor.AdvisorService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.lendinggallery.advisor.AdvisorDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.auditable.impl.AuditableServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.USER_ALREADY_EXIST_LIKE_ADVISOR_IN_ART_BOND;
import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.USER_DOES_NOT_ADVISOR_FOR_ART_BOND;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_ID_IS_EMPTY;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_FOUND;


@Slf4j
@Service
public class AdvisorServiceImpl extends AuditableServiceImpl<AdvisorDto, AdvisorEntity> implements AdvisorService {

    private static final Class<AdvisorDto> DTO_CLASS = AdvisorDto.class;
    private static final Class<AdvisorEntity> ENTITY_CLASS = AdvisorEntity.class;

    private final AdvisorRepository advisorRepository;

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    public AdvisorServiceImpl(AdvisorRepository advisorRepository, DefaultConverter defaultConverter) {
        super(advisorRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.advisorRepository = advisorRepository;
    }

    @Override
    public List<AdvisorDto> findByUserId(UUID userId) {
        if (userId == null) {
            throw new ClientException(USER_ID_IS_EMPTY);
        }
        List<AdvisorEntity> entities = advisorRepository.findAllByUserIdAndObjectState(userId, ObjectState.ACTIVE);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public boolean currentUserIsAdvisor(UUID artBondId) {
        boolean result = false;
        if (artBondId != null) {
            if (!SecurityUtil.isAnonymous()) {
                result = existByUserIdAndArtBondId(SecurityUtil.getUserId(), artBondId);
            }
        }
        return result;
    }

    @Override
    public void checkCurrentUserIsAdvisor(UUID artBondId) {
        if (artBondId != null) {
            SecurityUtil.checkUserByBanStatus();
            if (!currentUserIsAdvisor(artBondId)) {
                throw new ClientException(USER_DOES_NOT_ADVISOR_FOR_ART_BOND);
            }
        }
    }

    @Override
    public boolean existByUserIdAndArtBondId(UUID userId, UUID artBondId) {
        boolean result = false;
        if (ObjectUtils.allNotNull(userId, artBondId)) {
            result = advisorRepository.existsByUserIdAndArtBondIdAndObjectState(userId, artBondId, ObjectState.ACTIVE);
        }
        return result;
    }

    @Override
    @Transactional
    public AdvisorDto create(AdvisorDto dto) {
        checkAdvisor(dto);
        return super.create(dto);
    }

    @Override
    @Transactional
    public AdvisorDto update(AdvisorDto dto) {
        checkAdvisor(dto);
        return super.update(dto);
    }

    @Override
    @Transactional
    public AdvisorDto createWithUser(AdvisorDto advisor) {
        AdvisorDto result = null;
        if (advisor != null && advisor.getUser() != null) {
            PublicUserDto user = userExchangeService.createOrGetPublicUser(advisor.getUser());
            if (user != null) {
                advisor.setUserId(user.getId());
                result = create(advisor);
                result.setUser(user);

            }
        }
        return result;
    }

    @Override
    public Page<AdvisorDto> getByArtBondId(UUID artBondId, boolean setUsers, Integer page, Integer size) {
        Page<AdvisorDto> result = null;
        if (artBondId != null) {
            Page<AdvisorEntity> entities = advisorRepository.findAllByArtBondIdAndObjectState(artBondId, ObjectState.ACTIVE, PageRequest.of(page, size));
            result = converter.convert(entities, dtoClass);
            if ((result != null) && CollectionUtils.isNotEmpty(result.getContent())) {
                if (setUsers) {
                    setUsers(result.getContent());
                }
            }
        }
        return result;
    }

    @Override
    public Page<AdvisorDto> getAll(ObjectState objectState, Pageable pageable) {
        Page<AdvisorDto> result = super.getAll(objectState, pageable);
        if (result != null && result.hasContent()) {
            setUsers(result.getContent());
        }
        return result;
    }

    @Override
    public void setUsers(List<AdvisorDto> advisors) {
        Map<UUID, PublicUserDto> users = userExchangeService.findPublicUserMapByIds(advisors.stream().map(AdvisorDto::getUserId).collect(Collectors.toList()));
        if (MapUtils.isNotEmpty(users)) {
            advisors.forEach(f -> f.setUser(users.get(f.getUserId())));
        }
    }

    @Override
    public Boolean checkAdvisorExistByPhone(String phone) {
        boolean result = false;
        if (StringUtils.isNotBlank(phone)) {
            UserDto user = userExchangeService.getByPhone(phone);
            if (user != null) {
                List<AdvisorDto> advisor = findByUserId(user.getId());
                if (CollectionUtils.isNotEmpty(advisor)) {
                    result = true;
                }
            }
        }
        return result;
    }

    @Override
    public List<UUID> getArtBondIdsByUserId(UUID userId) {
        return advisorRepository.getArtBondIdsByUserIdAndObjectState(userId, ObjectState.ACTIVE);
    }

    private void checkAdvisor(AdvisorDto dto) {
        if (dto.getUserId() == null) {
            throw new ClientException(USER_ID_IS_EMPTY);
        }
        if (dto.getId() == null && existByUserIdAndArtBondId(dto.getUserId(), dto.getArtBondId())) {
            throw new ClientException(USER_ALREADY_EXIST_LIKE_ADVISOR_IN_ART_BOND);
        }
        if (!userExchangeService.userIsExist(dto.getUserId())) {
            throw new ClientException(USER_NOT_FOUND);
        }
    }
}
