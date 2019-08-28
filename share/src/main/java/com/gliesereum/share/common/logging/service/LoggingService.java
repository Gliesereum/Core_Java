package com.gliesereum.share.common.logging.service;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface LoggingService {

    void publishing(JsonNode jsonNode, String queueName);

    void publishingRequestObject(Object o);

    void publishingSystemObject(Object o);
}
