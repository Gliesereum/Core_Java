package com.gliesereum.share.common.model.dto.account.kyc;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KycRequestFieldDto extends DefaultDto {

    private UUID kycFieldId;

    private KycFieldDto kycField;

    private UUID kycRequestId;

    private String value;
}
