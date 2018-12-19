package com.gliesereum.share.common.logging.service;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 04/12/2018
 */
public interface LoggingService {

    void publishing(JsonNode jsonNode);

    void publishingObject(Object o);
}
