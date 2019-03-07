package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author vitalij
 */
public enum KycStatus {

    KYC_REQUESTED,
    KYC_IN_PROCESS,
    KYC_PASSED,
    KYC_REJECTED,
    KYC_REWORKED,
    KYC_SENT_TO_REWORK;

    public String toString() {
        return name().toLowerCase();
    }
}
