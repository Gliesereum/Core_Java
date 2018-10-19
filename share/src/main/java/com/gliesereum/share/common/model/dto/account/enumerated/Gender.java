package com.gliesereum.share.common.model.dto.account.enumerated;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
public enum Gender {

    MALE, FEMALE, UNKNOWN;

    public String toString() {
        return name().toLowerCase();
    }
}
