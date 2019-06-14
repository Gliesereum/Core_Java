package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public enum Gender {

    MALE, FEMALE, UNKNOWN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
