package com.gliesereum.account.facade.statistic.impl;

import com.gliesereum.account.facade.statistic.StatisticFacade;
import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.model.dto.account.statistic.AccountPublicStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class StatisticFacadeImpl implements StatisticFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private CorporationService corporationService;

    //TODO:refactor on use Auditable
    @Override
    public AccountPublicStatisticDto getStatistic() {
        AccountPublicStatisticDto result = new AccountPublicStatisticDto();
        result.setUserCount(userService.count());
        result.setCorporationCount(corporationService.count());
        return result;
    }
}
