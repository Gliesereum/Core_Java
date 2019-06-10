package com.gliesereum.share.common.model.dto.lendinggallery.customer;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.kyc.KycRequestDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.DetailedUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
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

    private List<OperationsStoryDto> operationsStories = new ArrayList<>();

}
