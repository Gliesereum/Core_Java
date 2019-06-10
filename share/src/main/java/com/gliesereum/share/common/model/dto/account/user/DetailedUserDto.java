package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.kyc.KycRequestDto;
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
public class DetailedUserDto extends DefaultDto {

    private String phone;

    private String email;

    private UserDto user;

    private List<CorporationDto> corporations = new ArrayList<>();

    private List<KycRequestDto> passedKycRequests = new ArrayList<>();
}
