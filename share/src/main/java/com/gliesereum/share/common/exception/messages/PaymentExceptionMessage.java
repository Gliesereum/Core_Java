package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 * @version 1.0
 */
public class PaymentExceptionMessage {

    private PaymentExceptionMessage() {
    }

    public static final ExceptionMessage OWNER_ID_EMPTY = new ExceptionMessage(1800, 400, "Owner id is empty");
    public static final ExceptionMessage PRIVATE_KEY_EMPTY = new ExceptionMessage(1801, 400, "Private key is empty");
    public static final ExceptionMessage PUBLIC_KEY_EMPTY = new ExceptionMessage(1802, 400, "Public key is empty");
    public static final ExceptionMessage OBJECT_TYPE_EMPTY = new ExceptionMessage(1803, 400, "Object type is empty");
    public static final ExceptionMessage FAVORITE_EXIST_IN_OBJECT = new ExceptionMessage(1804, 400, "Favorite already exist");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_CARD = new ExceptionMessage(1805, 400, "Don't have permission to card");
    public static final ExceptionMessage CARD_NOT_FOUND = new ExceptionMessage(1806, 400, "Card not found");
}
