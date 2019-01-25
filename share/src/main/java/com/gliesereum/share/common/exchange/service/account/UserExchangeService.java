package com.gliesereum.share.common.exchange.service.account;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface UserExchangeService {

    boolean userIsExist(UUID userId);

    boolean userKYCPassed(UUID userId);
}
