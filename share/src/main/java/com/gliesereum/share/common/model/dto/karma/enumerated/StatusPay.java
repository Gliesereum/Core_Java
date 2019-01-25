package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 */
public enum StatusPay {

    PAID,
    NOT_PAID;

    public String toString() {
        return name().toLowerCase();
    }
}
