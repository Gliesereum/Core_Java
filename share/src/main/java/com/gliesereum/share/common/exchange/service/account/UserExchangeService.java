package com.gliesereum.share.common.exchange.service.account;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 05/12/2018
 */
public interface UserExchangeService {

    boolean userIsExist(UUID userId);

    boolean userKYCPassed(UUID userId);
}
