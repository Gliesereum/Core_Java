package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author vitalij
 */
public enum KycStatus {

    KYC_REQUESTED(" REQUESTED"),
    KYC_IN_PROCESS(" IN PROCESS"),
    KYC_PASSED(" PASSED"),
    KYC_REJECTED(" REJECTED"),
    KYC_REWORKED("  REWORKED"),
    KYC_SENT_TO_REWORK(" SENT TO REWORK");

    public final String value;

    KycStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
