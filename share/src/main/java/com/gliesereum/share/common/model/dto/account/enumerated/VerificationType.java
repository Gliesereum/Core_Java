package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author vitalij
 */
public enum VerificationType {

    PHONE,EMAIL;

    public String toString() {
        return name().toLowerCase();
    }
}
