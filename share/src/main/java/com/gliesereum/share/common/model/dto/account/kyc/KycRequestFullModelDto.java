package com.gliesereum.share.common.model.dto.account.kyc;

import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import com.gliesereum.share.common.model.dto.account.enumerated.KycStatus;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class KycRequestFullModelDto {

    private UUID objectId;

    private KycRequestType kycRequestType;

    private KycStatus kycStatus;

    private String comment;

    private List<KycRequestFieldDto> fields = new ArrayList<>();

    private UserDto user;

    private CorporationDto corporation;
}
