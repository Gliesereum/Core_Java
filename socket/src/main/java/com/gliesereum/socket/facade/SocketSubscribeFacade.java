package com.gliesereum.socket.facade;

import com.gliesereum.socket.model.subscribe.SubscribeInfo;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface SocketSubscribeFacade {

    void validateSubscribe(SubscribeInfo subscribeInfo);
}
