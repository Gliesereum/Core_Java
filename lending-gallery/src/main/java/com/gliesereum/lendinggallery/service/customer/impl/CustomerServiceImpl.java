package com.gliesereum.lendinggallery.service.customer.impl;

import com.gliesereum.lendinggallery.model.entity.customer.CustomerEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.customer.CustomerRepository;
import com.gliesereum.lendinggallery.service.artbond.ArtBondService;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.lendinggallery.service.offer.OperationsStoryService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.CustomerPaymentInfo;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.PaymentCalendarDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
public class CustomerServiceImpl extends DefaultServiceImpl<CustomerDto, CustomerEntity> implements CustomerService {

    private static final Class<CustomerDto> DTO_CLASS = CustomerDto.class;
    private static final Class<CustomerEntity> ENTITY_CLASS = CustomerEntity.class;

    private final CustomerRepository customerRepository;

    private static final Integer PAYMENT_PERIOD_DAYS = 30;

    @Autowired
    private OperationsStoryService operationsStoryService;

    @Autowired
    private ArtBondService artBondService;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, DefaultConverter defaultConverter) {
        super(customerRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDto findByUserId(UUID id) {
        if (id == null) {
            throw new ClientException(ID_IS_EMPTY);
        }
        CustomerEntity entity = customerRepository.findByUserId(id);
        if (entity == null) {
            throw new ClientException(CUSTOMER_NOT_FOUND_BY_USER_ID);
        }
        return converter.convert(entity, dtoClass);
    }

    @Override
    public CustomerDto getByUser() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        CustomerEntity entity = customerRepository.findByUserId(SecurityUtil.getUserId());
        return converter.convert(entity, dtoClass);
    }

    @Override
    @Transactional
    public CustomerDto create(CustomerDto dto) {
        checkExist();
        return super.create(dto);
    }

    @Override
    public List<PaymentCalendarDto> getPaymentCalendar(UUID userId) {
        List<PaymentCalendarDto> result = null;
        CustomerDto customer = findByUserId(userId);
        List<OperationsStoryDto> operationStories = operationsStoryService.getAllByCustomerIdAndOperationType(customer.getId(), OperationType.PURCHASE);
        if (CollectionUtils.isNotEmpty(operationStories)) {
            Map<UUID, Long> purchasedStocks = operationStories
                    .stream().filter(i -> (i.getStockCount() != null) && (i.getStockCount() > 0))
                    .collect(Collectors.groupingBy(OperationsStoryDto::getArtBondId, Collectors.summingLong(OperationsStoryDto::getStockCount)));
            if (MapUtils.isNotEmpty(purchasedStocks)) {
                result = new ArrayList<>();
                for (Map.Entry<UUID, Long> purchasedStock : purchasedStocks.entrySet()) {
                    ArtBondDto artBond = artBondService.getById(purchasedStock.getKey());
                    List<PaymentCalendarDto> paymentCalendar = artBondService.getPaymentCalendar(artBond, artBond.getPaymentStartDate(), purchasedStock.getValue());
                    if (CollectionUtils.isNotEmpty(paymentCalendar)) {
                        result.addAll(paymentCalendar);
                    }
                }
                result.sort(Comparator.comparing(PaymentCalendarDto::getDate));
            }
        }
        return result;
    }

    @Override
    public CustomerPaymentInfo getPaymentInfoByArtBond(UUID artBondId, UUID userId) {
        CustomerPaymentInfo result = null;
        if (ObjectUtils.allNotNull(artBondId, userId)) {
            CustomerDto customer = findByUserId(userId);
            List<OperationsStoryDto> operationsStories = operationsStoryService.getAllByCustomerId(customer.getId());
            if (CollectionUtils.isNotEmpty(operationsStories)) {
                ArtBondDto artBond = artBondService.getArtBondById(artBondId);
                LocalDateTime currentDate = LocalDateTime.now();
                LocalDateTime paymentStartDate = artBond.getPaymentStartDate();
                if (paymentStartDate.isBefore(currentDate)) {
                    result = new CustomerPaymentInfo();
                    double balance = 0.0;
                    double profit = 0.0;
                    long stockCount = 0;
                    LocalDateTime lastPaymentDate = paymentStartDate;
                    for (OperationsStoryDto operationsStory : operationsStories) {
                        if (operationsStory.getOperationType().equals(OperationType.PURCHASE)) {
                            balance += operationsStory.getSum();
                            stockCount += operationsStory.getStockCount();
                        }
                        if (operationsStory.getOperationType().equals(OperationType.PAYMENT)) {
                            profit += operationsStory.getSum();
                            if (operationsStory.getCreate().isAfter(lastPaymentDate)) {
                                lastPaymentDate = operationsStory.getCreate();
                            }
                        }
                    }

                    long daysAfterPaymentStart = ChronoUnit.DAYS.between(paymentStartDate, currentDate);
                    long daysAfterLastPayment = ChronoUnit.DAYS.between(lastPaymentDate, currentDate);
                    long daysPayment = ChronoUnit.DAYS.between(paymentStartDate, artBond.getPaymentFinishDate());
                    int paymentPeriodDays = PAYMENT_PERIOD_DAYS * artBond.getPaymentPeriod();

                    double purchaseValue = stockCount * artBond.getStockPrice();
                    double dividendValue = purchaseValue / 100 * artBond.getDividendPercent();
                    double rewardValue = purchaseValue / 100 * artBond.getRewardPercent();

                    double nkd = (dividendValue / paymentPeriodDays) * daysAfterLastPayment + (rewardValue / daysPayment) * daysAfterPaymentStart;

                    result.setBalance(balance);
                    result.setProfit(profit);
                    result.setNkd(nkd);
                }
            }
        }
        return result;
    }

    @Override
    public CustomerPaymentInfo getPaymentInfoCommon(UUID userId) {
        CustomerPaymentInfo result = null;
        CustomerDto customer = findByUserId(userId);
        List<OperationsStoryDto> operationStories = operationsStoryService.getAllByCustomerIdAndOperationType(customer.getId(), OperationType.PURCHASE);
        if (CollectionUtils.isNotEmpty(operationStories)) {
            List<UUID> artBondIds = operationStories.stream()
                    .map(OperationsStoryDto::getArtBondId)
                    .distinct()
                    .collect(Collectors.toList());
            result = new CustomerPaymentInfo(0.0, 0.0, 0.0 );
            for (UUID artBondId : artBondIds) {
                CustomerPaymentInfo paymentInfoByArtBond = getPaymentInfoByArtBond(artBondId, userId);
                result.setNkd(result.getNkd() + paymentInfoByArtBond.getNkd());
                result.setProfit(result.getProfit() + paymentInfoByArtBond.getProfit());
                result.setBalance(result.getBalance() + paymentInfoByArtBond.getBalance());
            }
        }
        return result;
    }

    private void checkExist() {
        CustomerDto saveCustomer = getByUser();
        if (saveCustomer != null) {
            throw new ClientException(CUSTOMER_ALREADY_EXIST);
        }
    }
}
