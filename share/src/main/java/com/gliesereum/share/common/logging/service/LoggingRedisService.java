package com.gliesereum.share.common.logging.service;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 04/12/2018
 */
public interface LoggingRedisService {

    void publishing(String message);

    void publishingObject(Object o);
}
