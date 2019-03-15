package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 */
public enum StatusProcess {

    CANCELED,
    WAITING,
    STARTED,
    IN_PROCESS,
    COMPLETED;

    public String toString() {
        return name().toLowerCase();
    }
}
