package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 * @version 1.0
 */
public class PaymentExceptionMessage {

    private PaymentExceptionMessage() {
    }

    public static final ExceptionMessage OBJECT_ID_EMPTY = new ExceptionMessage(1800, 400, "Object id is empty");
    public static final ExceptionMessage PRIVATE_KEY_EMPTY = new ExceptionMessage(1801, 400, "Private key is empty");
    public static final ExceptionMessage PUBLIC_KEY_EMPTY = new ExceptionMessage(1802, 400, "Public key is empty");
    public static final ExceptionMessage OBJECT_TYPE_EMPTY = new ExceptionMessage(1803, 400, "Object type is empty");
}
