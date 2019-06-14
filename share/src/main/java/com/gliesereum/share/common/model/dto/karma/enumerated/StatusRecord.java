package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 */
public enum StatusRecord {

    CREATED,
    COMPLETED,
    CANCELED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
