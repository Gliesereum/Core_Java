package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 */
public enum StatusSpace {

    FREE,
    BUSY,
    NOT_WORK;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
