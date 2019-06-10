package com.gliesereum.lendinggallery.service.customer;

import com.gliesereum.lendinggallery.model.entity.customer.CustomerEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.DetailedCustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.CustomerPaymentInfo;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.PaymentCalendarDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CustomerService extends DefaultService<CustomerDto, CustomerEntity> {

    CustomerDto findByUserId(UUID id);

    CustomerDto getByUser();

    List<PaymentCalendarDto> getPaymentCalendar(UUID userId);

    CustomerPaymentInfo getPaymentInfoByArtBond(UUID artBondId, UUID userId);

    CustomerPaymentInfo getPaymentInfoCommon(UUID userId);

    List<DetailedCustomerDto> getAllDetailed();
}
