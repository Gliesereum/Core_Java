package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author vitalij
 */
public enum VerifiedStatus {

    VERIFIED,
    UNVERIFIED;

    public String toString() {
        return name().toLowerCase();
    }
}
