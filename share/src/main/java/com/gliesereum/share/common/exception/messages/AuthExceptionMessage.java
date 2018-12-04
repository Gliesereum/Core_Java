package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 * @since 12/10/2018
 */
public class AuthExceptionMessage {

    public static final ExceptionMessage VALUE_EMPTY = new ExceptionMessage(1260, 400, "Value is empty");
    public static final ExceptionMessage CODE_EMPTY = new ExceptionMessage(1261, 400, "Code is empty");
    public static final ExceptionMessage TYPE_EMPTY = new ExceptionMessage(1262, 400, "Type is empty");
    public static final ExceptionMessage USER_TYPE_EMPTY = new ExceptionMessage(1263, 400, "User type is empty");
    public static final ExceptionMessage CODE_WORSE = new ExceptionMessage(1264, 400, "Verification code not correct try again");

}
