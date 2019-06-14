package com.gliesereum.share.common.model.dto.account.kyc;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KycValuesRequestDto extends DefaultDto {

    private KycRequestType requestType;

    private UUID objectId;

    private Map<UUID, String> values;
}
