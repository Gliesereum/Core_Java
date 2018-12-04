package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.Gender;
import com.gliesereum.share.common.model.dto.account.enumerated.UserType;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
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

    private VerifiedStatus verifiedStatus;

    @NotEmpty
    private UserType userType;

    public UserDto(@NotEmpty UserType userType) {
        this.userType = userType;
    }
}
