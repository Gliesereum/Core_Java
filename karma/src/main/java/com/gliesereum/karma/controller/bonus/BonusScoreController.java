package com.gliesereum.karma.controller.bonus;

import com.gliesereum.karma.service.bonus.BonusScoreService;
import com.gliesereum.share.common.model.dto.karma.bonus.BonusScoreDto;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
@RequestMapping("/bonus-score")
public class BonusScoreController {

    @Autowired
    private BonusScoreService bonusScoreService;

    @GetMapping("/me")
    public BonusScoreDto getByCurrentUser() {
        SecurityUtil.checkUserByBanStatus();
        return bonusScoreService.getOrCreateByUserId(SecurityUtil.getUserId());
    }
}
