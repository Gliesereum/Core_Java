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
    public static final ExceptionMessage CARDS_NOT_FOUND_IN_USER = new ExceptionMessage(1807, 400, "User does not have any cards");
    public static final ExceptionMessage VERIFY_CARDS_NOT_FOUND_IN_USER = new ExceptionMessage(1808, 400, "User does not have verify cards");
    public static final ExceptionMessage ORDER_NOT_FOUND_BY_ID = new ExceptionMessage(1809, 400, "Order not found by id");
    public static final ExceptionMessage ORDER_DOES_NOT_HAVE_ANY_TRANSACTION = new ExceptionMessage(1810, 400, "Order does not have any transaction");
    public static final ExceptionMessage ORDER_DOES_NOT_HAVE_AUTH_TRANSACTION = new ExceptionMessage(1812, 400, "Order does not have AUTH transaction");
    public static final ExceptionMessage YOU_CAN_NOT_SETTLE_MORE_THEN_IN_AUTH_TRANSACTION = new ExceptionMessage(1813, 400, "You can't SETTLE more then in AUTH transaction");
    public static final ExceptionMessage YOU_CAN_NOT_REFUND_MORE_THEN_IN_AUTH_TRANSACTION = new ExceptionMessage(1814, 400, "You can't REFUND more then in AUTH transaction");
    public static final ExceptionMessage ORDER_DOES_NOT_HAVE_APPROVED_PAY_TRANSACTION = new ExceptionMessage(1815, 400, "Order does not have approved transaction");
}
