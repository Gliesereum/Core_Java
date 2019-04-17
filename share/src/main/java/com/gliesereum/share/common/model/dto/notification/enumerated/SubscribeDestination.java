package com.gliesereum.share.common.model.dto.notification.enumerated;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public enum  SubscribeDestination {
    USER_RECORD("userRecord"), BUSINESS_RECORD("businessRecord");

    private String name;

    SubscribeDestination(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
