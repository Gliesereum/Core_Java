package com.gliesereum.lendinggallery.facade.customer;

import com.gliesereum.share.common.model.dto.lendinggallery.customer.DetailedCustomerDto;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CustomerFacade {

    List<DetailedCustomerDto> getDetailedInvestor(UUID artBondId);

    List<DetailedCustomerDto> getDetailedBorrower();

    List<DetailedCustomerDto> getDetailedAdmin();

    List<DetailedCustomerDto> getDetailedInvestorByCurrentAdviser();
}
