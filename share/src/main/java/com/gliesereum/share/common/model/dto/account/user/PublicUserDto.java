package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PublicUserDto extends DefaultDto {

    private String firstName;

    private String lastName;

    private String middleName;

    private String phone;

    private String email;

    private String avatarUrl;

}
