package com.gliesereum.share.common.model.enumerated;

/**
 * @author vitalij
 * @version 1.0
 */
public enum CriteriaType {

    EQ,
    LIKE,
    LT,
    LTE,
    GT,
    GTE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
