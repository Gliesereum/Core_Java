package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 * @since 2018-12-17
 */
public enum StatusSpace {

    FREE,
    BUSY,
    NOT_WORK;

    public String toString() {
        return name().toLowerCase();
    }
}
