package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author vitalij
 * @since 10/16/18
 */
public enum KFCStatus {

    KFC_NOT_PASSED,
    KFC_IN_PROCESS,
    KFC_PASSED;

    public String toString() {
        return name().toLowerCase();
    }
}
