package com.gliesereum.lendinggallery.service.offer.impl;

import com.gliesereum.lendinggallery.model.entity.offer.InvestorOfferEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.offer.InvestorOfferRepository;
import com.gliesereum.lendinggallery.service.advisor.AdvisorService;
import com.gliesereum.lendinggallery.service.artbond.ArtBondService;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.lendinggallery.service.media.MediaService;
import com.gliesereum.lendinggallery.service.offer.InvestorOfferService;
import com.gliesereum.lendinggallery.service.offer.OfferCommentService;
import com.gliesereum.lendinggallery.service.offer.OperationsStoryService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.*;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.*;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;
import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class InvestorOfferServiceImpl extends DefaultServiceImpl<InvestorOfferDto, InvestorOfferEntity> implements InvestorOfferService {

    private static final Class<InvestorOfferDto> DTO_CLASS = InvestorOfferDto.class;
    private static final Class<InvestorOfferEntity> ENTITY_CLASS = InvestorOfferEntity.class;

    private final InvestorOfferRepository investorOfferRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ArtBondService artBondService;

    @Autowired
    private OperationsStoryService operationsStoryService;

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    private AdvisorService advisorService;

    @Autowired
    private OfferCommentService commentService;

    @Autowired
    private MediaService mediaService;

    public InvestorOfferServiceImpl(InvestorOfferRepository investorOfferRepository, DefaultConverter defaultConverter) {
        super(investorOfferRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.investorOfferRepository = investorOfferRepository;
    }

    @Override
    public List<InvestorOfferDto> getAllByState(OfferStateType state) {
        List<InvestorOfferEntity> entities = investorOfferRepository.findAllByStateType(state);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public InvestorOfferDto updateState(OfferStateType state, UUID id, String comment) {
        if (state == null) {
            throw new ClientException(OFFER_STATE_IS_EMPTY);
        }
        InvestorOfferDto result = findById(id);
        /*if (result != null) {
            advisorService.checkCurrentUserIsAdvisor(result.getArtBondId());
        }*/ //todo check is adviser
        checkUpdateState(result, state);
        result.setStateType(state);
        result = super.update(result);
        if ((result != null) && state.equals(OfferStateType.COMPLETED)) {
            operationsStoryService.create(
                    new OperationsStoryDto(result.getCustomerId(),
                            result.getArtBondId(),
                            result.getSumInvestment().doubleValue(),
                            result.getStockCount(),
                            OperationType.PURCHASE.name().toLowerCase(),
                            OperationType.PURCHASE.name().toLowerCase(),
                            LocalDateTime.now(),
                            OperationType.PURCHASE));

        }
        addComment(result, comment);
        return result;
    }

    private void checkUpdateState(InvestorOfferDto offer, OfferStateType state) {
        if (offer == null) {
            throw new ClientException(OFFER_NOT_FOUND_BY_ID);
        }
        ArtBondDto artBond = artBondService.getById(offer.getArtBondId());
        Integer commonSum = 0;

        if (state.equals(OfferStateType.COMPLETED)) {

            List<InvestorOfferDto> offers = getAllByArtBondAndStateType(artBond.getId(), OfferStateType.COMPLETED);

            if (CollectionUtils.isNotEmpty(offers)) {
                commonSum = offers.stream().mapToInt(InvestorOfferDto::getSumInvestment).sum();
            }
            if ((artBond.getPrice() - commonSum) < offer.getSumInvestment()) {
                throw new ClientException(SUM_EXCEEDS_AMOUNT_ALLOWED_FOR_INVESTMENT);
            }
        }
        if (artBond.getPrice() == (commonSum + offer.getSumInvestment())) {
            artBond.setStatusType(StatusType.COMPLETED_COLLECTION);
            artBondService.superUpdateArtBond(artBond);
            List<InvestorOfferDto> offers = getAllByArtBond(artBond.getId());
            if (CollectionUtils.isNotEmpty(offers)) {
                List<InvestorOfferDto> refused = new ArrayList<>();
                offers.forEach(f -> {
                    if (!f.getStateType().equals(OfferStateType.COMPLETED)) {
                        f.setStateType(OfferStateType.REFUSED);
                        refused.add(f);
                    }
                });
                super.update(refused);
            }
        }
    }

    @Override
    public List<InvestorOfferDto> getAllByArtBond(UUID id) {
        List<InvestorOfferEntity> entities = investorOfferRepository.findAllByArtBondId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<InvestorOfferDto> getAllByArtBondAndStateType(UUID id, OfferStateType stateType) {
        List<InvestorOfferEntity> entities = investorOfferRepository.findAllByArtBondIdAndStateType(id, stateType);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<InvestorOfferDto> getAllByUser() {
        CustomerDto customer = getCustomer();
        List<InvestorOfferEntity> entities = null;
        if (customer != null) {
            entities = investorOfferRepository.findAllByCustomerIdOrderByCreate(customer.getId());
        }
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<InvestorOfferDto> getAllByArtBondAndCurrentUser(UUID artBondId) {
        CustomerDto customer = getCustomer();
        List<InvestorOfferEntity> entities = null;
        if (customer != null) {
            entities = investorOfferRepository.findAllByArtBondIdAndCustomerIdOrderByCreate(artBondId, customer.getId());
        }
        return converter.convert(entities, dtoClass);
    }

    @Override
    public InvestorOfferDto create(InvestorOfferDto dto) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        checkModel(dto);
        CustomerDto customer = getCustomer();
        if (customer == null) {
            throw new ClientException(CUSTOMER_NOT_FOUND_BY_USER_ID);
        }
        dto.setCustomerId(customer.getId());
        dto.setCreate(LocalDateTime.now());
        dto.setStateType(OfferStateType.REQUEST);
        return super.create(dto);
    }

    @Override
    public InvestorOfferDto update(InvestorOfferDto dto) {
        checkModel(dto);
        InvestorOfferDto saveDto = findById(dto.getId());
        dto.setCustomerId(saveDto.getCustomerId());
        dto.setStateType(saveDto.getStateType());
        return super.update(dto);
    }

    @Override
    public List<InvestorOfferFullModelDto> getAllFullModelByState(OfferStateType state) {
        List<InvestorOfferEntity> entities = investorOfferRepository.findAllByStateType(state);
        return setFullModelByEntities(entities);
    }

    @Override
    public List<InvestorOfferFullModelDto> getAllFullModelByState(List<OfferStateType> states) {
        List<InvestorOfferEntity> entities = investorOfferRepository.findAllByStateTypeIn(states);
        return setFullModelByEntities(entities);
    }
    
    @Override
    public Page<InvestorOfferFullModelDto> getFullModelByCustomerId(UUID customerId, Pageable pageable) {
        Page<InvestorOfferFullModelDto> result = null;
        Page<InvestorOfferEntity> page = investorOfferRepository.findAllByCustomerIdOrderByCreate(customerId, pageable);
        if (page != null) {
            List<InvestorOfferFullModelDto> list = setFullModelByEntities(page.getContent());
            if (CollectionUtils.isNotEmpty(list)) {
                result = new PageImpl<>(list, page.getPageable(), page.getTotalElements());
            }
        }
        return result;
    }
    
    @Override
    public List<InvestorOfferFullModelDto> searchInvestorOffersFullModelByCurrentAdviser(OfferSearchDto search) {
        List<InvestorOfferFullModelDto> result = null;
        //advisorService.checkCurrentUserIsAdvisor(search.getArtBondId()); //todo check adviser
        List<InvestorOfferEntity> entities = investorOfferRepository.searchInvestorOffersByParams(search);
        result = setFullModelByEntities(entities);
        setUsersToCommentInOffers(result);
        if (CollectionUtils.isNotEmpty(result)) {
            result.sort(Comparator.comparing(InvestorOfferFullModelDto::getCreate).reversed());
        }
        return result;
    }

    @Override
    public InvestorOfferDto setComment(UUID id, String comment) {
        InvestorOfferDto result = findById(id);
        addComment(result, comment);
        return result;
    }

    @Override
    public InvestorOfferFullModelDto getInvestorOfferFullModelById(UUID id) {
        InvestorOfferFullModelDto result = null;
        InvestorOfferEntity entity = investorOfferRepository.getOne(id);
        if(entity != null){
            List<InvestorOfferFullModelDto> fullModelList = setFullModelByEntities(Arrays.asList(entity));
            if(CollectionUtils.isNotEmpty(fullModelList)){
                result = fullModelList.get(0);
            }
        }
        return result;
    }

    private void addComment(InvestorOfferDto offer, String comment) {
        if (offer != null && StringUtils.isNotBlank(comment)) {
            //advisorService.checkCurrentUserIsAdvisor(offer.getArtBondId()); //todo check adviser
            OfferCommentDto offerComment = new OfferCommentDto();
            offerComment.setComment(comment);
            offerComment.setCreate(LocalDateTime.now());
            offerComment.setOfferId(offer.getId());
            offerComment.setStateType(offer.getStateType());
            offerComment.setCreateById(SecurityUtil.getUserId());
            OfferCommentDto createComment = commentService.create(offerComment);
            offer.getComments().add(createComment);
            offer.getComments().sort(Comparator.comparing(OfferCommentDto::getCreate).reversed());
            setUsersToComment(offer.getComments());
        }
    }

    private List<InvestorOfferFullModelDto> setFullModelByEntities(List<InvestorOfferEntity> entities) {
        List<InvestorOfferFullModelDto> result = null;
        if (CollectionUtils.isNotEmpty(entities)) {
            result = converter.convert(entities, InvestorOfferFullModelDto.class);
            if (CollectionUtils.isNotEmpty(result)) {
                result.forEach(i -> {
                    UUID artBondId = i.getArtBondId();
                    if (artBondId != null) {
                        i.setArtBond(artBondService.getById(artBondId));
                        i.getArtBond().setAmountCollected(artBondService.getAmountCollected(artBondId));
                    }
                });
                customerService.setCustomerAndUser(result, InvestorOfferFullModelDto::getCustomerId,
                        InvestorOfferFullModelDto::setCustomer, InvestorOfferFullModelDto::setUser);
            }
        }
        if(CollectionUtils.isNotEmpty(result)){
            setMedia(result);
        }
        return result;
    }

    private InvestorOfferDto findById(UUID id) {
        if (id == null) {
            throw new ClientException(ID_IS_EMPTY);
        }
        InvestorOfferDto result = getById(id);
        if (result == null) {
            throw new ClientException(OFFER_NOT_FOUND_BY_ID);
        }
        return result;
    }

    private void checkModel(InvestorOfferDto dto) {
        if (dto == null) {
            throw new ClientException(MODEL_IS_EMPTY);
        }
        ArtBondDto artBond = artBondService.getById(dto.getArtBondId());
        if (artBond == null) {
            throw new ClientException(ART_BOND_NOT_FOUND_BY_ID);
        }
        if (!artBond.getStatusType().equals(StatusType.ACTIVE_COLLECTION) ||
                artBond.getSpecialStatusType().equals(SpecialStatusType.BLOCKED)) {
            throw new ClientException(ART_BOND_NOT_AVAILABLE_FOR_INVESTMENT);
        }
        if (dto.getStockCount() == null || dto.getStockCount() == 0) {
            throw new ClientException(SUM_OF_INVESTMENT_CAN_NOT_BE_ZERO);
        }

        List<InvestorOfferDto> offers = getAllByArtBondAndStateType(artBond.getId(), OfferStateType.COMPLETED);
        Integer commonSum = 0;
        if (CollectionUtils.isNotEmpty(offers)) {
            commonSum = offers.stream().mapToInt(InvestorOfferDto::getSumInvestment).sum();
        }

        Double sumInvesting = artBond.getStockPrice() * dto.getStockCount();

        if ((artBond.getPrice() - commonSum) < sumInvesting) {
            throw new ClientException(SUM_EXCEEDS_AMOUNT_ALLOWED_FOR_INVESTMENT);
        }

        dto.setSumInvestment(sumInvesting.intValue());
    }

    private CustomerDto getCustomer() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return customerService.findByUserId(SecurityUtil.getUserId());
    }

    private void setUsersToComment(List<OfferCommentDto> comments) {
        if (CollectionUtils.isNotEmpty(comments)) {
            Set<UUID> usersIds = comments.stream().map(OfferCommentDto::getCreateById).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(usersIds)) {
                Map<UUID, PublicUserDto> users = userExchangeService.findPublicUserMapByIds(usersIds);
                if (MapUtils.isNotEmpty(users)) {
                    comments.forEach(f -> {
                        f.setCreateBy(users.get(f.getCreateById()));
                    });
                }
            }
        }
    }

    private void setUsersToCommentInOffers(List<InvestorOfferFullModelDto> offers) {
        if (CollectionUtils.isNotEmpty(offers)) {
            Set<UUID> userIds = new HashSet<>();
            offers.forEach(offer -> {
                if (CollectionUtils.isNotEmpty(offer.getComments())) {
                    offer.getComments().forEach(f -> {
                        userIds.add(f.getCreateById());
                    });
                }
            });
            if (CollectionUtils.isNotEmpty(userIds)) {
                Map<UUID, PublicUserDto> users = userExchangeService.findPublicUserMapByIds(userIds);
                if (MapUtils.isNotEmpty(users)) {
                    offers.forEach(offer -> {
                        if (CollectionUtils.isNotEmpty(offer.getComments())) {
                            offer.getComments().forEach(f -> {
                                f.setCreateBy(users.get(f.getCreateById()));
                            });
                        }
                    });
                }
            }
        }
    }

    private void setMedia(List<InvestorOfferFullModelDto> offers) {
        if (CollectionUtils.isNotEmpty(offers)) {
            Set<ArtBondDto> setArtBond = offers.stream().map(InvestorOfferFullModelDto::getArtBond).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(setArtBond)) {
                Map<UUID, ArtBondDto> map = new HashMap<>();
                setArtBond.forEach(artBondDto -> {
                    artBondDto.setImages(mediaService.getByObjectIdAndType(artBondDto.getId(), BlockMediaType.IMAGES));
                    map.put(artBondDto.getId(), artBondDto);
                });
                if (MapUtils.isNotEmpty(map)) {
                    offers.forEach(offer -> {
                        offer.setArtBond(map.get(offer.getArtBondId()));
                    });
                }
            }
        }
    }
}
