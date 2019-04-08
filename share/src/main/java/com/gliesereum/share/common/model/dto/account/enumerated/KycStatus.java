package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author vitalij
 */
public enum KycStatus {

    KYC_REQUESTED("KYS requested"),
    KYC_IN_PROCESS("KYS in process"),
    KYC_PASSED("KYS passed"),
    KYC_REJECTED("KYS rejected"),
    KYC_REWORKED("KYS reworked"),
    KYC_SENT_TO_REWORK("KYS sent to rework");

    public final String value;

    KycStatus(String value) {
        this.value = value;
    }

    public String toString() {
        return name().toLowerCase();
    }
}
