package com.gliesereum.lendinggallery.service.customer.impl;

import com.gliesereum.lendinggallery.model.entity.customer.CustomerEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.customer.CustomerRepository;
import com.gliesereum.lendinggallery.service.artbond.ArtBondService;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.lendinggallery.service.offer.OperationsStoryService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.CustomerType;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;
import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.CUSTOMER_ALREADY_EXIST;
import static com.gliesereum.share.common.exception.messages.LandingGalleryExceptionMessage.ID_IS_EMPTY;

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

    @Autowired
    private OperationsStoryService operationsStoryService;

    @Autowired
    private ArtBondService artBondService;

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, DefaultConverter defaultConverter) {
        super(customerRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDto> getByCustomerType(CustomerType customerType) {
        List<CustomerDto> result = null;
        if (customerType != null) {
            List<CustomerEntity> entities = customerRepository.findAllByCustomerType(customerType);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<CustomerDto> getByCustomerTypeAndIdIn(CustomerType customerType, List<UUID> ids) {
        List<CustomerDto> result = null;
        if ((customerType != null) && CollectionUtils.isNotEmpty(ids)) {
            List<CustomerEntity> entities = customerRepository.findAllByCustomerTypeAndIdIn(customerType, ids);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public Page<CustomerDto> getByCustomerType(CustomerType customerType, Pageable pageable) {
        Page<CustomerDto> result = null;
        if (customerType != null) {
            Page<CustomerEntity> entities = customerRepository.findAllByCustomerType(customerType, pageable);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public Page<CustomerDto> getByCustomerTypeAndIdIn(CustomerType customerType, List<UUID> ids, Pageable pageable) {
        Page<CustomerDto> result = null;
        if ((customerType != null) && CollectionUtils.isNotEmpty(ids)) {
            Page<CustomerEntity> entities = customerRepository.findAllByCustomerTypeAndIdIn(customerType, ids, pageable);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<CustomerDto> getByUserIds(List<UUID> userIds) {
        List<CustomerDto> result = null;
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<CustomerEntity> entities = customerRepository.findAllByUserIdIn(userIds);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public CustomerDto findByUserId(UUID id) {
        if (id == null) {
            throw new ClientException(ID_IS_EMPTY);
        }
        CustomerEntity entity = customerRepository.findByUserId(id);
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
        dto.setUserId(SecurityUtil.getUserId());
        return super.create(dto);
    }

    @Override
    public CustomerDto update(CustomerDto dto) {
        if (dto != null) {
            dto.setUserId(SecurityUtil.getUserId());
        }
        return super.update(dto);
    }

    @Override
    public List<PaymentCalendarDto> getPaymentCalendar(UUID userId) {
        List<PaymentCalendarDto> result = null;
        CustomerDto customer = findByUserId(userId);
        if (customer != null) {
            List<OperationsStoryDto> operationStories = operationsStoryService.getAllByCustomerIdAndOperationType(customer.getId(), OperationType.PURCHASE);
            if (CollectionUtils.isNotEmpty(operationStories)) {
                Map<UUID, Long> purchasedStocks = operationStories
                        .stream().filter(i -> (i.getStockCount() != null) && (i.getStockCount() > 0))
                        .collect(Collectors.groupingBy(OperationsStoryDto::getArtBondId, Collectors.summingLong(OperationsStoryDto::getStockCount)));
                if (MapUtils.isNotEmpty(purchasedStocks)) {
                    result = new ArrayList<>();
                    for (Map.Entry<UUID, Long> purchasedStock : purchasedStocks.entrySet()) {
                        ArtBondDto artBond = artBondService.getById(purchasedStock.getKey());
                        List<PaymentCalendarDto> paymentCalendar = artBondService.getPaymentCalendar(artBond, artBond.getPaymentStartDate(), purchasedStock.getValue(), true);
                        if (CollectionUtils.isNotEmpty(paymentCalendar)) {
                            result.addAll(paymentCalendar);
                        }
                    }
                    result.sort(Comparator.comparing(PaymentCalendarDto::getDate));
                }
            }
        }
        return result;
    }

    @Override
    public CustomerPaymentInfo getPaymentInfoByArtBond(UUID artBondId, UUID userId) {
        CustomerPaymentInfo result = null;
        if (ObjectUtils.allNotNull(artBondId, userId)) {
            CustomerDto customer = findByUserId(userId);
            if (customer != null) {
                List<OperationsStoryDto> operationsStories = operationsStoryService.getAllByCustomerIdAndArtBondId(customer.getId(), artBondId);
                if (CollectionUtils.isNotEmpty(operationsStories)) {
                    result = new CustomerPaymentInfo();
                    ArtBondDto artBond = artBondService.getArtBondById(artBondId);
                    LocalDateTime currentDate = LocalDateTime.now(ZoneId.of("UTC"));
                    LocalDateTime paymentStartDate = artBond.getPaymentStartDate();

                    double balance = 0.0;
                    double profit = 0.0;
                    long stockCount = 0;
                    LocalDateTime lastPaymentDate = paymentStartDate;
                    for (OperationsStoryDto operationsStory : operationsStories) {
                        if (operationsStory.getOperationType().equals(OperationType.PURCHASE)) {
                            balance += operationsStory.getSum();
                            stockCount += operationsStory.getStockCount();
                        } else {
                            if (operationsStory.getOperationType().equals(OperationType.PAYMENT) || operationsStory.getOperationType().equals(OperationType.PAYMENT_REWARD)) {
                                profit += operationsStory.getSum();
                                if (operationsStory.getOperationType().equals(OperationType.PAYMENT) && operationsStory.getCreate().isAfter(lastPaymentDate)) {
                                    lastPaymentDate = operationsStory.getCreate();
                                }
                            }
                        }
                    }

                    double purchaseValue = stockCount * artBond.getStockPrice();
                    double dividendValuePerYear = (purchaseValue / 100 * artBond.getDividendPercent());
                    double dividendValue = dividendValuePerYear / (12.0 / artBond.getPaymentPeriod());
                    double rewardValue = purchaseValue / 100 * artBond.getRewardPercent();

                    long paymentPeriodMonth = ChronoUnit.MONTHS.between(paymentStartDate.toLocalDate(), artBond.getPaymentFinishDate().toLocalDate());

                    double profitOnDividend = paymentPeriodMonth / artBond.getPaymentPeriod() * dividendValue;

                    double resultProfit = profitOnDividend + rewardValue;

                    if (paymentStartDate.isBefore(currentDate)) {
                        long daysAfterPaymentStart = ChronoUnit.DAYS.between(paymentStartDate.toLocalDate(), currentDate.toLocalDate());
                        long daysAfterLastPayment = ChronoUnit.DAYS.between(lastPaymentDate.toLocalDate(), currentDate.toLocalDate());
                        long daysPayment = ChronoUnit.DAYS.between(paymentStartDate.toLocalDate(), artBond.getPaymentFinishDate().toLocalDate());

                        //long daysPerPaymentYear = ChronoUnit.DAYS.between(lastPaymentDate.toLocalDate(), lastPaymentDate.toLocalDate().plus(1, ChronoUnit.YEARS));
    
                        long daysPerPaymentYear = Year.of(lastPaymentDate.toLocalDate().plus(artBond.getPaymentPeriod(), ChronoUnit.MONTHS).getYear()).length();

                        double nkd = artBondService.calculateNkd(dividendValuePerYear, daysPerPaymentYear, daysAfterLastPayment, rewardValue, daysPayment, daysAfterPaymentStart);
                        result.setNkd(nkd);
                    }

                    result.setBalance(balance);
                    result.setProfit(profit);
                    result.setResultProfit(resultProfit);

                }
            }
        }
        return result;
    }

    @Override
    public CustomerPaymentInfo getPaymentInfoCommon(UUID userId) {
        CustomerPaymentInfo result = new CustomerPaymentInfo(0.0, 0.0, 0.0, 0.0);
        CustomerDto customer = findByUserId(userId);
        if (customer != null) {
            List<OperationsStoryDto> operationStories = operationsStoryService.getAllByCustomerIdAndOperationType(customer.getId(), OperationType.PURCHASE);
            if (CollectionUtils.isNotEmpty(operationStories)) {
                List<UUID> artBondIds = operationStories.stream()
                        .map(OperationsStoryDto::getArtBondId)
                        .distinct()
                        .collect(Collectors.toList());
                for (UUID artBondId : artBondIds) {
                    CustomerPaymentInfo paymentInfoByArtBond = getPaymentInfoByArtBond(artBondId, userId);
                    result.setNkd(result.getNkd() + paymentInfoByArtBond.getNkd());
                    result.setProfit(result.getProfit() + paymentInfoByArtBond.getProfit());
                    result.setBalance(result.getBalance() + paymentInfoByArtBond.getBalance());
                    result.setResultProfit(result.getResultProfit() + paymentInfoByArtBond.getResultProfit());
                }
            }
        }
        return result;
    }

    @Override
    public <T extends DefaultDto> void setCustomerAndUser(List<T> list,
                                                          Function<T, UUID> customerIdGetter,
                                                          BiConsumer<T, CustomerDto> customerSetter,
                                                          BiConsumer<T, UserDto> userSetter) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<UUID> customerIds = list.stream().map(customerIdGetter).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(customerIds)) {
                List<CustomerDto> customers = super.getByIds(customerIds);
                if (CollectionUtils.isNotEmpty(customers)) {
                    Map<UUID, CustomerDto> customerMap = customers.stream().collect(Collectors.toMap(CustomerDto::getId, i -> i));
                    List<UUID> userIds = customers.stream().map(CustomerDto::getUserId).collect(Collectors.toList());
                    Map<UUID, UserDto> userMap = userExchangeService.findUserMapByIds(userIds);
                    list.forEach(i -> {
                        UUID customerId = customerIdGetter.apply(i);
                        if (customerId != null) {
                            CustomerDto customer = customerMap.get(customerId);
                            if (customer != null) {
                                customerSetter.accept(i, customer);
                                userSetter.accept(i, userMap.get(customer.getUserId()));
                            }
                        }
                    });
                }
            }
        }
    }

    private void checkExist() {
        CustomerDto saveCustomer = getByUser();
        if (saveCustomer != null) {
            throw new ClientException(CUSTOMER_ALREADY_EXIST);
        }
    }
}
