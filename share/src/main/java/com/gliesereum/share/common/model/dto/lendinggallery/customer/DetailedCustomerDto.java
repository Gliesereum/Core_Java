package com.gliesereum.share.common.model.dto.lendinggallery.customer;

import com.gliesereum.share.common.model.dto.account.user.DetailedUserDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.CustomerPaymentInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DetailedCustomerDto extends DetailedUserDto {

    private CustomerDto customer;

    private CustomerPaymentInfo paymentInfo;

    private List<OperationsStoryDto> operationsStories = new ArrayList<>();

}
