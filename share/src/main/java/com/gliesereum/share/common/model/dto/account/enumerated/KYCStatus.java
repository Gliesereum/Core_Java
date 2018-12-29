package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author vitalij
 * @since 10/16/18
 */
public enum KYCStatus {

    KYC_NOT_PASSED,
    KYC_IN_PROCESS,
    KYC_PASSED,
    KYC_REJECTED,
    KYC_POSTPONED;

    public String toString() {
        return name().toLowerCase();
    }
}
