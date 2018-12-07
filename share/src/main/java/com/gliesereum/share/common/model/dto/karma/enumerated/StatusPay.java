package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 * @since 2018-12-07
 */
public enum StatusPay {

    PAID,
    NOT_PAID;

    public String toString() {
        return name().toLowerCase();
    }
}
