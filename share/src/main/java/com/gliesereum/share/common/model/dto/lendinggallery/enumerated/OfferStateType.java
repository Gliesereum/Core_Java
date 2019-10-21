package com.gliesereum.share.common.model.dto.lendinggallery.enumerated;

/**
 * @author vitalij
 * @version 1.0
 */
public enum OfferStateType {

    REQUEST(1),
    IN_PROCESS(2),
    COMPLETED(3),
    REFUSED(4);

    private final int index;

    OfferStateType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
