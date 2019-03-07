package com.gliesereum.share.common.model.dto.account.kyc;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import com.gliesereum.share.common.model.dto.account.enumerated.KycStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class KycRequestDto extends DefaultDto {

    private UUID objectId;

    private KycRequestType kycRequestType;

    private KycStatus kycStatus;

    private String comment;

    private List<KycRequestFieldDto> fields = new ArrayList<>();
}
