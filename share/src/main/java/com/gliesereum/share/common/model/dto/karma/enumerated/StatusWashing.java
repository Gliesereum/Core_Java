package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 */
public enum StatusWashing {

    STARTED,
    IN_PROCESS,
    COMPLETED,
    CANCELED;

    public String toString() {
        return name().toLowerCase();
    }
}
