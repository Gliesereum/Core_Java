package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.Gender;
import com.gliesereum.share.common.model.dto.account.enumerated.KFCStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends DefaultDto {

    private String username;

    private String firstname;

    private String lastname;

    private Gender gender;

    private BanStatus banStatus;

    private VerifiedStatus verifiedStatus;

    private KFCStatus kfcStatus;
}
