package com.gliesereum.karma.controller.statistic;

import com.gliesereum.karma.facade.statistic.StatisticFacade;
import com.gliesereum.share.common.model.dto.karma.statistic.KarmaPublicStatisticDto;
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
    public KarmaPublicStatisticDto getPublicStatistic() {
        return statisticFacade.getPublicStatistic();
    }
}
