package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 */
public enum ServiceType {

    CAR_WASH, CAR_SERVICE, TIRE_FITTING;

    public String toString() {
        return name().toLowerCase();
    }
}
