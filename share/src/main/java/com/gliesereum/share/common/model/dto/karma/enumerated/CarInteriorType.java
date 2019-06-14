package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 */
public enum CarInteriorType {

    SUEDE,
    TEXTILE,
    LEATHER,
    ARTIFICIAL_LEATHER,
    ALCANTARA,
    TASKANA,
    VELOURS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
