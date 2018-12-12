package com.gliesereum.share.common.model.enumerated;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
public enum CriteriaType {

    EQ,
    LIKE,
    LT,
    LTE,
    GT,
    GTE;

    public String toString() {
        return name().toLowerCase();
    }
}
