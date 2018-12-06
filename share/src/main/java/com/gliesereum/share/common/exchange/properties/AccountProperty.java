package com.gliesereum.share.common.exchange.properties;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 05/12/2018
 */

@Data
@NoArgsConstructor
public class AccountProperty {

    private String userIsExist;

    private String userKYCPassed;
}
