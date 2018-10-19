package com.gliesereum.account.mq;

/**
 * @author vitalij
 * @since 10/17/18
 */
public interface MessagePublisher {

    void publish(final String message);
}
