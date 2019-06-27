package com.gliesereum.share.common.model.dto.account.referral;

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
public class ReferralCodeUserDto extends DefaultDto {

    private UUID userId;

    private UUID referrerId;

    private UUID referralCodeId;
}
