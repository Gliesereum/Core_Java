package com.gliesereum.share.common.model.dto.account.referral;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReferralCodeDto extends DefaultDto {

    private UUID userId;

    private String code;

    private LocalDateTime createDate;
}
