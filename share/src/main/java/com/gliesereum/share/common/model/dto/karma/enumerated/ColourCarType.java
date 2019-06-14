package com.gliesereum.share.common.model.dto.karma.enumerated;

/**
 * @author vitalij
 * @version 1.0
 */
public enum  ColourCarType {

    WHITE,
    BLACK,
    GRAY,
    SILVER,
    GOLDEN,
    RED,
    BLUE,
    BROWN,
    BEIGE,
    YELLOW,
    GREEN,
    OTHER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
