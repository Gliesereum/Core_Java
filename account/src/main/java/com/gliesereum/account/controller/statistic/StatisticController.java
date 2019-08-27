package com.gliesereum.account.controller.statistic;

import com.gliesereum.account.facade.statistic.StatisticFacade;
import com.gliesereum.share.common.model.dto.account.statistic.AccountPublicStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    private StatisticFacade statisticFacade;

    @GetMapping("/public")
    public AccountPublicStatisticDto getPublic() {
        return statisticFacade.getStatistic();
    }
}
