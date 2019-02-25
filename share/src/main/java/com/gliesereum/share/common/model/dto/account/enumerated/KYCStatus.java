package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author vitalij
 */
public enum KYCStatus {

    KYC_NOT_PASSED,
    KYC_IN_PROCESS,
    KYC_PASSED,
    KYC_REJECTED,
    KYC_SENT_TO_REWORK;

    public String toString() {
        return name().toLowerCase();
    }
}
