package com.gliesereum.lendinggallery.controller.customer;

import com.gliesereum.lendinggallery.facade.customer.CustomerFacade;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.DetailedCustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.CustomerPaymentInfo;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.PaymentCalendarDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerFacade customerFacade;

   /* @GetMapping
    public List<CustomerDto> getAll() {
        return customerService.getAll();
    }*/

    @GetMapping
    public Page<CustomerDto> getAll(@PageableDefault(page = 0, size = 100) Pageable pageable) {
        return customerService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public CustomerDto getById(@PathVariable("id") UUID id) {
        return customerService.getById(id);
    }

    @GetMapping("/user")
    public CustomerDto getByCurrentUser() {
        return customerService.getByUser();
    }

    @PostMapping
    public CustomerDto create(@Valid @RequestBody CustomerDto dto) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return customerService.create(dto);
    }

    @PutMapping
    public CustomerDto update(@Valid @RequestBody CustomerDto dto) {
        return customerService.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        customerService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/payment-calendar")
    public List<PaymentCalendarDto> paymentCalendar() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return customerService.getPaymentCalendar(SecurityUtil.getUserId());
    }
    
    @GetMapping("/payment-calendar/by-user")
    public List<PaymentCalendarDto> paymentCalendarByCustomer(@RequestParam("userId") UUID userId) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return customerService.getPaymentCalendar(userId);
    }

    @GetMapping("/payment-info/by-art-bond")
    public CustomerPaymentInfo getPaymentInfoByArtBond(@RequestParam("artBondId") UUID artBondId) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return customerService.getPaymentInfoByArtBond(artBondId, SecurityUtil.getUserId());
    }

    @GetMapping("/payment-info/common")
    public CustomerPaymentInfo getPaymentInfoCommon() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return customerService.getPaymentInfoCommon(SecurityUtil.getUserId());
    }

    @GetMapping("/detailed/investor-by-current-adviser")
    public List<DetailedCustomerDto> getDetailedInvestorByCurrentAdviser() {
        return customerFacade.getDetailedInvestorByCurrentAdviser();
    }

    @GetMapping("/detailed/investor")
    public Page<DetailedCustomerDto> getDetailedInvestor(@PageableDefault(page = 0, size = 100) Pageable pageable,
                                                         @RequestParam(value = "artBondId", required = false) UUID artBondId) {
        return customerFacade.getDetailedInvestor(artBondId, pageable);
    }

    @GetMapping("/detailed/borrower")
    public Page<DetailedCustomerDto> getDetailedBorrower(@PageableDefault(page = 0, size = 100) Pageable pageable) {
        return customerFacade.getDetailedBorrower(pageable);
    }

    @GetMapping("/detailed/admin")
    public List<DetailedCustomerDto> getDetailedAdmin() {
        return customerFacade.getDetailedAdmin();
    }
}
