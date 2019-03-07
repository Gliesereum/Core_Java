package com.gliesereum.share.common.exchange.service.karma;

import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface KarmaExchangeService {

    List<BaseBusinessDto> getBusinessForCurrentUser();
}
