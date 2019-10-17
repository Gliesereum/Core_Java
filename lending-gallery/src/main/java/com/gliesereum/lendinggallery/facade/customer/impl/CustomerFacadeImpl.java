package com.gliesereum.lendinggallery.facade.customer.impl;

import com.gliesereum.lendinggallery.facade.customer.CustomerFacade;
import com.gliesereum.lendinggallery.service.advisor.AdvisorService;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.lendinggallery.service.offer.OperationsStoryService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.exchange.service.permission.GroupUserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.DetailedUserDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.DetailedCustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.CustomerType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.util.SecurityUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class CustomerFacadeImpl implements CustomerFacade {

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    private OperationsStoryService operationsStoryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DefaultConverter defaultConverter;

    @Autowired
    private GroupUserExchangeService groupUserExchangeService;

    @Autowired
    private AdvisorService advisorService;

    @Override
    public Page<DetailedCustomerDto> getDetailedInvestor(UUID artBondId, Pageable pageable) {
        Page<DetailedCustomerDto> result = Page.empty(pageable);
        List<DetailedCustomerDto> listDetailed = null;
        List<UUID> artBondIds = null;
        if (artBondId != null) {
            artBondIds = Arrays.asList(artBondId);
        }
        Page<CustomerDto> pageCustomers = getInvestors(artBondIds, pageable);
        if (pageCustomers != null && pageCustomers.hasContent()) {
            List<CustomerDto> customers = pageCustomers.getContent();
            if (CollectionUtils.isNotEmpty(customers)) {
                Map<UUID, CustomerDto> userCustomer = customers.stream().collect(Collectors.toMap(CustomerDto::getUserId, i -> i));
                List<DetailedUserDto> detailedUser = userExchangeService.findDetailedByIds(userCustomer.keySet());
                if (CollectionUtils.isNotEmpty(detailedUser)) {
                    listDetailed = defaultConverter.convert(detailedUser, DetailedCustomerDto.class);
                    listDetailed = listDetailed.stream()
                            //.filter(i -> CollectionUtils.isNotEmpty(i.getPassedKycRequests()))TODO; kyc request disable check
                            .peek(i -> {
                                i.setCustomer(userCustomer.get(i.getId()));
                                if (artBondId == null) {
                                    i.setPaymentInfo(customerService.getPaymentInfoCommon(i.getId()));
                                } else {
                                    i.setPaymentInfo(customerService.getPaymentInfoByArtBond(artBondId, i.getId()));
                                }
                            })
                            .collect(Collectors.toList());
                    insertOperationsStory(listDetailed);
                }
                if (CollectionUtils.isNotEmpty(listDetailed)) {
                    result = new PageImpl<>(listDetailed, pageCustomers.getPageable(), pageCustomers.getTotalElements());
                }
            }
        }
        return result;
    }

    @Override
    public Page<DetailedCustomerDto> getDetailedBorrower(Pageable pageable) {
        Page<DetailedCustomerDto> result = Page.empty(pageable);
        List<DetailedCustomerDto> listDetailed = null;
        Page<CustomerDto> pageCustomers = customerService.getByCustomerType(CustomerType.BORROWER, pageable);
        if (pageCustomers != null && pageCustomers.hasContent()) {
            List<CustomerDto> customers = pageCustomers.getContent();
            if (CollectionUtils.isNotEmpty(customers)) {
                Map<UUID, CustomerDto> userCustomer = customers.stream().collect(Collectors.toMap(CustomerDto::getUserId, i -> i));
                List<DetailedUserDto> detailedUser = userExchangeService.findDetailedByIds(userCustomer.keySet());
                if (CollectionUtils.isNotEmpty(detailedUser)) {
                    listDetailed = defaultConverter.convert(detailedUser, DetailedCustomerDto.class);
                    listDetailed = listDetailed.stream()
                            //.filter(i -> CollectionUtils.isNotEmpty(i.getPassedKycRequests()))TODO; kyc request disable check
                            .peek(i -> i.setCustomer(userCustomer.get(i.getId())))
                            .collect(Collectors.toList());
                    insertOperationsStory(listDetailed);
                }
                if (CollectionUtils.isNotEmpty(listDetailed)) {
                    result = new PageImpl<>(listDetailed, pageCustomers.getPageable(), pageCustomers.getTotalElements());
                }
            }
        }
        return result;
    }

    @Override
    public List<DetailedCustomerDto> getDetailedAdmin() {
        List<DetailedCustomerDto> result = null;
        List<UUID> adminIds = groupUserExchangeService.getUserIdsByGroupPurpose(GroupPurpose.LG_ADMIN);
        if (CollectionUtils.isNotEmpty(adminIds)) {
            List<CustomerDto> customers = customerService.getByUserIds(adminIds);
            if (CollectionUtils.isNotEmpty(customers)) {
                Map<UUID, CustomerDto> userCustomer = customers.stream().collect(Collectors.toMap(CustomerDto::getUserId, i -> i));
                List<DetailedUserDto> detailedUser = userExchangeService.findDetailedByIds(userCustomer.keySet());
                if (CollectionUtils.isNotEmpty(detailedUser)) {
                    result = defaultConverter.convert(detailedUser, DetailedCustomerDto.class);
                    result = result.stream()
                            .peek(i -> i.setCustomer(userCustomer.get(i.getId())))
                            .collect(Collectors.toList());
                }
            }

        }
        return result;
    }

    @Override
    public List<DetailedCustomerDto> getDetailedInvestorByCurrentAdviser() {
        List<DetailedCustomerDto> result = null;
        SecurityUtil.checkUserByBanStatus();
        List<UUID> artBondIds = advisorService.getArtBondIdsByUserId(SecurityUtil.getUserId());
        List<CustomerDto> customers = null;
        if (CollectionUtils.isNotEmpty(artBondIds)) {
            customers = getInvestors(artBondIds);
        }
        if (CollectionUtils.isNotEmpty(customers)) {
            Map<UUID, CustomerDto> userCustomer = customers.stream().collect(Collectors.toMap(CustomerDto::getUserId, i -> i));
            List<DetailedUserDto> detailedUser = userExchangeService.findDetailedByIds(userCustomer.keySet());
            if (CollectionUtils.isNotEmpty(detailedUser)) {
                result = defaultConverter.convert(detailedUser, DetailedCustomerDto.class);
                result = result.stream()
                        .filter(i -> CollectionUtils.isNotEmpty(i.getPassedKycRequests()))
                        .peek(i -> {
                            i.setCustomer(userCustomer.get(i.getId()));
                            i.setPaymentInfo(customerService.getPaymentInfoCommon(i.getId()));
                        })
                        .collect(Collectors.toList());
                insertOperationsStory(result);
            }
        }
        return result;
    }

    private void insertOperationsStory(List<DetailedCustomerDto> result) {
        List<UUID> resultCustomerIds = result.stream().map(i -> i.getCustomer().getId()).collect(Collectors.toList());
        Map<UUID, List<OperationsStoryDto>> operationStory = operationsStoryService.getAllByCustomerIds(resultCustomerIds);
        result.forEach(i -> i.setOperationsStories(operationStory.get(i.getCustomer().getId())));
    }

    private List<CustomerDto> getInvestors(List<UUID> artBondIds) {
        List<CustomerDto> result;
        if (CollectionUtils.isNotEmpty(artBondIds)) {
            List<UUID> customerIds = operationsStoryService.getCustomerByArtBondId(artBondIds);
            result = customerService.getByCustomerTypeAndIdIn(CustomerType.INVESTOR, customerIds);
        } else {
            result = customerService.getByCustomerType(CustomerType.INVESTOR);
        }
        return result;
    }

    private Page<CustomerDto> getInvestors(List<UUID> artBondIds, Pageable pageable) {
        Page<CustomerDto> result;
        if (CollectionUtils.isNotEmpty(artBondIds)) {
            List<UUID> customerIds = operationsStoryService.getCustomerByArtBondId(artBondIds);
            result = customerService.getByCustomerTypeAndIdIn(CustomerType.INVESTOR, customerIds, pageable);
        } else {
            result = customerService.getByCustomerType(CustomerType.INVESTOR, pageable);
        }
        return result;
    }
}
