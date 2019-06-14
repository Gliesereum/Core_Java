package com.gliesereum.share.common.model.dto.lendinggallery.offer;

import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OperationsStoryFullDto extends OperationsStoryDto {

    private UserDto user;

    private CustomerDto customer;
}
