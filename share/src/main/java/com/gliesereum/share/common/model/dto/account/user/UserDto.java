package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.Gender;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends DefaultDto {

    @Size(min = 2)
    @NotEmpty
    private String firstName;

    @Size(min = 2)
    @NotEmpty
    private String lastName;

    @Size(min = 3)
    @NotEmpty
    private String middleName;

    @Size(min = 3)
    @NotEmpty
    private String position;

    @Size(min = 3)
    @NotEmpty
    private String country;

    @Size(min = 3)
    @NotEmpty
    private String city;

    @Size(min = 6)
    @NotEmpty
    private String address;

    @Size(min = 3)
    @NotEmpty
    private String addAddress;

    @NotEmpty
    private String avatarUrl;

    @NotEmpty
    private String coverUrl;

    private Gender gender;

    private BanStatus banStatus;

    private KYCStatus kycStatus;

    private List<CorporationDto> corporation = new ArrayList<>();
}
