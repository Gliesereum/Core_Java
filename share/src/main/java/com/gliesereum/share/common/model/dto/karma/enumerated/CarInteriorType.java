package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
public enum CarInteriorType {

    SUEDE,
    TEXTILE,
    LEATHER,
    ARTIFICIAL_LEATHER,
    ALCANTARA,
    TASKANA,
    VELOURS;

    public String toString() {
        return name().toLowerCase();
    }
}
