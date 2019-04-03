package com.gliesereum.karma.facade.impl;

import com.gliesereum.karma.facade.GroupUserExchangeFacade;
import com.gliesereum.share.common.exchange.service.permission.GroupUserExchangeService;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class GroupUserExchangeFacadeImpl implements GroupUserExchangeFacade {

    @Autowired
    private GroupUserExchangeService groupUserExchangeService;

    @Async
    @Override
    public void addUserByGroupPurposeAsync(UUID userId, GroupPurpose groupPurpose) {
        if (ObjectUtils.allNotNull(userId, groupPurpose)) {
            groupUserExchangeService.addUserByGroupPurpose(userId, groupPurpose);
        }
    }
}
