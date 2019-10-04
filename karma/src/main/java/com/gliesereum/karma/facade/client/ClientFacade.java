package com.gliesereum.karma.facade.client;

import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ClientFacade {
    
    void addNewClient(PublicUserDto user, UUID businessId);
    
    void addNewClient(UserDto user, UUID businessId);
    
    void updateClientInfo(UserDto user);
    
    void importClients();
    
    void indexingClients();
}
