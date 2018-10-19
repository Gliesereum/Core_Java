package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author vitalij
 * @since 10/16/18
 */
public enum BanStatus {

    UNBAN,
    BAN;

    public String toString() {
        return name().toLowerCase();
    }
}
