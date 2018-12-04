package com.gliesereum.account.appender;

/**
 * @author vitalij
 * @since 11/9/18
 */
public interface PublisherRedisService {

    void publishing(String message);

    void publishingObject(Object o);
}
