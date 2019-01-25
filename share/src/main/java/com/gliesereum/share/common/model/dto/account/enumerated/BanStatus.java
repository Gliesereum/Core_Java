package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author vitalij
 */
public enum BanStatus {

    UNBAN,
    BAN;

    public String toString() {
        return name().toLowerCase();
    }
}
