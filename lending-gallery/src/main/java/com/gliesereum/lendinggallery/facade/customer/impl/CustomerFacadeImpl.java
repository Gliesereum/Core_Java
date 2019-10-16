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
    public List<DetailedCustomerDto> getDetailedInvestor(UUID artBondId) {
        List<DetailedCustomerDto> result = null;
        List<UUID> artBondIds = null;
        if (artBondId != null) {
            artBondIds = Arrays.asList(artBondId);
        }
        List<CustomerDto> customers = getInvestors(artBondIds);
        if (CollectionUtils.isNotEmpty(customers)) {
            Map<UUID, CustomerDto> userCustomer = customers.stream().collect(Collectors.toMap(CustomerDto::getUserId, i -> i));
            List<DetailedUserDto> detailedUser = userExchangeService.findDetailedByIds(userCustomer.keySet());
            if (CollectionUtils.isNotEmpty(detailedUser)) {
                result = defaultConverter.convert(detailedUser, DetailedCustomerDto.class);
                result = result.stream()
                        .filter(i -> CollectionUtils.isNotEmpty(i.getPassedKycRequests()))
                        .peek(i -> {
                            i.setCustomer(userCustomer.get(i.getId()));
                            if (artBondId == null) {
                                i.setPaymentInfo(customerService.getPaymentInfoCommon(i.getId()));
                            } else {
                                i.setPaymentInfo(customerService.getPaymentInfoByArtBond(artBondId, i.getId()));
                            }
                        })
                        .collect(Collectors.toList());
                insertOperationsStory(result);
            }
        }
        return result;
    }

    @Override
    public List<DetailedCustomerDto> getDetailedBorrower() {
        List<DetailedCustomerDto> result = null;
        List<CustomerDto> customers = customerService.getByCustomerType(CustomerType.BORROWER);
        if (CollectionUtils.isNotEmpty(customers)) {
            Map<UUID, CustomerDto> userCustomer = customers.stream().collect(Collectors.toMap(CustomerDto::getUserId, i -> i));
            List<DetailedUserDto> detailedUser = userExchangeService.findDetailedByIds(userCustomer.keySet());
            if (CollectionUtils.isNotEmpty(detailedUser)) {
                result = defaultConverter.convert(detailedUser, DetailedCustomerDto.class);
                result = result.stream()
                        .filter(i -> CollectionUtils.isNotEmpty(i.getPassedKycRequests()))
                        .peek(i -> i.setCustomer(userCustomer.get(i.getId())))
                        .collect(Collectors.toList());
                insertOperationsStory(result);
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
        if(CollectionUtils.isNotEmpty(artBondIds)){
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
}
