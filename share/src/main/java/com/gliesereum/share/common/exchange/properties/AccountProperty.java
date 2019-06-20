package com.gliesereum.share.common.exchange.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class AccountProperty {

    private String userIsExist;

//    private String userKYCPassed;

    private String findByIds;

    private String findDetailedByIds;

    private String getByPhone;

    private String findUserPhonesByUserIds;

    private String findPublicUserByUserIds;
}
