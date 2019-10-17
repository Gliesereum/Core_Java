package com.gliesereum.lendinggallery.facade.customer;

import com.gliesereum.share.common.model.dto.lendinggallery.customer.DetailedCustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CustomerFacade {

    Page<DetailedCustomerDto> getDetailedInvestor(UUID artBondId, Pageable pageable);

    Page<DetailedCustomerDto> getDetailedBorrower(Pageable pageable);

    List<DetailedCustomerDto> getDetailedAdmin();

    List<DetailedCustomerDto> getDetailedInvestorByCurrentAdviser();
}
