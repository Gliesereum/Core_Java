package com.gliesereum.karma.controller.agent;

import com.gliesereum.karma.service.audit.AgentService;
import com.gliesereum.share.common.model.dto.karma.agent.AgentDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping("/current-user-agent")
    public MapResponse currentUserIsAgent() {
        SecurityUtil.checkUserByBanStatus();
        return new MapResponse(agentService.existByUserIdAndActive(SecurityUtil.getUserId()));
    }

    @PostMapping("/request")
    public AgentDto request() {
        SecurityUtil.checkUserByBanStatus();
        return agentService.createRequest(SecurityUtil.getUserId());
    }
}
