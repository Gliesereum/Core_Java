package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 */
public class LandingGalleryExceptionMessage {

    public static final ExceptionMessage ID_IS_EMPTY = new ExceptionMessage(1600, 404, "Id is empty");
    public static final ExceptionMessage MODEL_IS_EMPTY = new ExceptionMessage(1601, 404, "Model is empty");
    public static final ExceptionMessage MEDIA_NOT_FOUND_BY_ID = new ExceptionMessage(1602, 404, "Media not found by id");
    public static final ExceptionMessage ART_BOND_NOT_FOUND_BY_ID = new ExceptionMessage(1603, 404, "Art bond not found by id");
    public static final ExceptionMessage BLOCK_MEDIA_TYPE_IS_EMPTY = new ExceptionMessage(1604, 404, "Block media type is empty");
    public static final ExceptionMessage CUSTOMER_NOT_FOUND_BY_ID = new ExceptionMessage(1605, 404, "Customer not found by id");
    public static final ExceptionMessage OFFER_STATE_IS_EMPTY = new ExceptionMessage(1606, 404, "Offer state is empty");
    public static final ExceptionMessage OFFER_NOT_FOUND_BY_ID = new ExceptionMessage(1607, 404, "Offer not found by id");
    public static final ExceptionMessage CUSTOMER_NOT_FOUND_BY_USER_ID = new ExceptionMessage(1608, 404, "Customer not found by user id");

}
