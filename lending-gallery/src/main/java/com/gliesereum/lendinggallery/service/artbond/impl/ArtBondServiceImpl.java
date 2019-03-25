package com.gliesereum.lendinggallery.service.artbond.impl;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.artbond.ArtBondRepository;
import com.gliesereum.lendinggallery.service.artbond.ArtBondService;
import com.gliesereum.lendinggallery.service.media.MediaService;
import com.gliesereum.lendinggallery.service.offer.InvestorOfferService;
import com.gliesereum.lendinggallery.service.offer.OperationsStoryService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.*;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.PaymentCalendarDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.fx.FxQuote;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;
import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.ART_BOND_NOT_FOUND_BY_ID;
import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.ID_IS_EMPTY;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class ArtBondServiceImpl extends DefaultServiceImpl<ArtBondDto, ArtBondEntity> implements ArtBondService {

    private static final Class<ArtBondDto> DTO_CLASS = ArtBondDto.class;
    private static final Class<ArtBondEntity> ENTITY_CLASS = ArtBondEntity.class;
    private static final Integer PAYMENT_PERIOD_DAYS = 30;

    private final ArtBondRepository artBondRepository;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private InvestorOfferService investorOfferService;

    @Autowired
    private OperationsStoryService operationsStoryService;


    public ArtBondServiceImpl(ArtBondRepository artBondRepository, DefaultConverter defaultConverter) {
        super(artBondRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.artBondRepository = artBondRepository;
    }

    @Override
    public List<ArtBondDto> getAllByStatus(StatusType status) {
        List<ArtBondEntity> entities = artBondRepository.findAllByStatusTypeAndSpecialStatusType(status, SpecialStatusType.ACTIVE);
        List<ArtBondDto> result = converter.convert(entities, dtoClass);
        result.forEach(f -> setAdditionalField(f));
        return result;
    }

    @Override
    public List<ArtBondDto> getAll() {
        List<ArtBondDto> result = super.getAll();
        result.forEach(f -> setAdditionalField(f));
        return result;
    }

    @Override
    public ArtBondDto create(ArtBondDto dto) {
        dto.setStatusType(StatusType.WAITING_COLLECTION);
        dto.setSpecialStatusType(SpecialStatusType.ACTIVE);
        return super.create(dto);
    }

    @Override
    public ArtBondDto update(ArtBondDto dto) {
        ArtBondDto artBond = getById(dto.getId());
        dto.setStatusType(artBond.getStatusType());
        dto.setSpecialStatusType(artBond.getSpecialStatusType());
        ArtBondDto result = super.update(dto);
        setAdditionalField(result);
        return result;
    }

    @Override
    public ArtBondDto getById(UUID id) {
        if (id == null) {
            throw new ClientException(ID_IS_EMPTY);
        }
        return super.getById(id);
    }

    @Override
    public ArtBondDto getArtBondById(UUID id) {
        ArtBondDto result = getById(id);
        if (result == null) {
            throw new ClientException(ART_BOND_NOT_FOUND_BY_ID);
        }
        setAdditionalField(result);
        return result;
    }

    @Override
    @Transactional
    public ArtBondDto updateStatus(StatusType status, UUID id) {
        ArtBondDto artBond = getById(id);
        if (artBond == null) {
            throw new ClientException(ART_BOND_NOT_FOUND_BY_ID);
        }
        if (artBond.getStatusType().equals(status)) {
            return artBond;
        }
        artBond.setStatusType(status);
        if (status.equals(StatusType.COMPLETED_COLLECTION)) {
            List<InvestorOfferDto> offers = investorOfferService.getAllByArtBond(id);
            if (CollectionUtils.isNotEmpty(offers)) {
                offers.forEach(f -> {
                    OperationsStoryDto story = new OperationsStoryDto();
                    story.setName(artBond.getName());
                    story.setSum(f.getSumInvestment());
                    story.setDescription(null);
                    story.setArtBondId(f.getArtBondId());
                    story.setCreate(LocalDateTime.now());
                    story.setOperationType(OperationType.PURCHASE);
                    story.setCustomerId(f.getCustomerId());
                    operationsStoryService.create(story);
                });
            }
        }
        return super.update(artBond);
    }

    @Override
    public List<ArtBondDto> getAllByTags(List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return new ArrayList<>();
        }
        List<ArtBondEntity> entities = artBondRepository.findAllByTagsContains(tags);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<ArtBondDto> getAllByUser() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        List<InvestorOfferDto> offers = investorOfferService.getAllByUser();
        List<UUID> ids = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(offers)) {
            ids = offers.stream().filter(io -> !io.getStateType().equals(OfferStateType.REFUSED)).map(m -> m.getArtBondId()).collect(Collectors.toList());
        }
        return getByIds(ids);
    }

    @Override
    public Map<String, Integer> currencyExchange(Long sum) {
        Map<String, Integer> result = new HashMap<>();
        try {
            FxQuote rubToUsd = YahooFinance.getFx("USDRUB=X");
            FxQuote rubToEur = YahooFinance.getFx("EURRUB=X");
            FxQuote usdToEur = YahooFinance.getFx("USDEUR=X");
            FxQuote eurToUsd = YahooFinance.getFx("EURUSD=X");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<PaymentCalendarDto> getPaymentCalendar(UUID id) {
        List<PaymentCalendarDto> result = null;
        if (id != null) {
            ArtBondDto artBond = super.getById(id);
            result = getPaymentCalendar(artBond);
        }
        return result;
    }

    public List<PaymentCalendarDto> getPaymentCalendar(ArtBondDto artBond) {
        List<PaymentCalendarDto> result = null;
            if (artBond != null) {
                result = getPaymentCalendar(artBond, artBond.getPaymentStartDate(), 1L);
            }
        return result;
    }

    @Override
    public List<PaymentCalendarDto> getPaymentCalendar(ArtBondDto artBond, LocalDateTime paymentStartDate, Long stockCount) {
        List<PaymentCalendarDto> result = new ArrayList<>();
        if (ObjectUtils.allNotNull(artBond, paymentStartDate)) {
            if (ObjectUtils.allNotNull(artBond.getDividendPercent(), artBond.getPaymentPeriod(), artBond.getPaymentFinishDate())) {
                LocalDateTime paymentDate = paymentStartDate.plus(artBond.getPaymentPeriod(), ChronoUnit.MONTHS);
                while (paymentDate.isBefore(artBond.getPaymentFinishDate())) {
                    PaymentCalendarDto paymentCalendar = new PaymentCalendarDto();
                    paymentCalendar.setArtBond(artBond);
                    paymentCalendar.setDividendPercent(artBond.getDividendPercent());
                    paymentCalendar.setDate(paymentDate);
                    double purchaseValue = stockCount * artBond.getStockPrice();
                    paymentCalendar.setDividendValue(purchaseValue / 100 * artBond.getDividendPercent());
                    result.add(paymentCalendar);
                    paymentDate = paymentDate.plus(artBond.getPaymentPeriod(), ChronoUnit.MONTHS);
                }
            }
        }
        return result;
    }

    @Override
    public Double getNkd(UUID artBondId) {
        ArtBondDto artBond = getById(artBondId);
        return getNkd(artBond);
    }

    public Double getNkd(ArtBondDto artBond) {
        Double result = null;
        if (artBond != null) {
            double dividendValue = artBond.getStockPrice() / 100 * artBond.getDividendPercent();
            int paymentPeriod = artBond.getPaymentPeriod();
            long daysAfterLastPayment = PAYMENT_PERIOD_DAYS * paymentPeriod;
            double rewardValue = artBond.getStockPrice() / 100 * artBond.getRewardPercent();
            long daysAfterPaymentStart =  PAYMENT_PERIOD_DAYS * paymentPeriod;
            long daysPayment = ChronoUnit.DAYS.between(artBond.getPaymentStartDate(), artBond.getPaymentFinishDate());
            result = calculateNkd(dividendValue, paymentPeriod, daysAfterLastPayment, rewardValue, daysPayment, daysAfterPaymentStart);
        }
        return result;
    }

    @Override
    public double calculateNkd(double dividendValue, int paymentPeriod, long daysAfterLastPayment, double rewardValue, long daysPayment, long daysAfterPaymentStart) {
        long paymentPeriodDays = PAYMENT_PERIOD_DAYS * paymentPeriod;
        return (dividendValue / paymentPeriodDays) * daysAfterLastPayment + (rewardValue / daysPayment) * daysAfterPaymentStart;
    }

    @Override
    public Map<String, Integer> getPercentPerYear(UUID artBondId) {
        ArtBondDto artBond = getById(artBondId);
        return getPercentPerYear(artBond);
    }

    public Map<String, Integer> getPercentPerYear(ArtBondDto artBond) {
        Map<String, Integer> result = null;
        if (artBond != null) {
            result = new HashMap<>();
            int countDividendPayment = 12 % artBond.getPaymentPeriod();
            result.put("min", artBond.getRewardPercent());
            result.put("max", artBond.getRewardPercent() + (countDividendPayment * artBond.getDividendPercent()));
        }
        return result;
    }

    @Override
    public Double getAmountCollected(UUID id) {
        Double result = null;
        if (id != null) {
            List<OperationsStoryDto> allPurchaseByArtBond = operationsStoryService.getAllPurchaseByArtBond(id);
            if (CollectionUtils.isNotEmpty(allPurchaseByArtBond)) {
                result = allPurchaseByArtBond.stream().mapToDouble(OperationsStoryDto::getSum).sum();
            }
        }
        return result;
    }

    private void setAdditionalField(ArtBondDto dto) {
        setMedia(dto);

        dto.setPaymentCalendar(getPaymentCalendar(dto));
        dto.setNkd(getNkd(dto));
        dto.setPercentPerYear(getPercentPerYear(dto));
        dto.setAmountCollected(getAmountCollected(dto.getId()));
    }

    private void setMedia(ArtBondDto dto) {
        dto.setImages(mediaService.getByObjectIdAndType(dto.getId(), BlockMediaType.IMAGES));
        dto.setArtBondInfo(mediaService.getByObjectIdAndType(dto.getId(), BlockMediaType.ART_BOND_INFO));
        dto.setAuthorInfo(mediaService.getByObjectIdAndType(dto.getId(), BlockMediaType.AUTHOR_INFO));
        dto.setDocuments(mediaService.getByObjectIdAndType(dto.getId(), BlockMediaType.DOCUMENTS));
    }
}
