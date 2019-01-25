package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 */
public class AuthExceptionMessage {

    public static final ExceptionMessage VALUE_EMPTY = new ExceptionMessage(1160, 400, "Value is empty");
    public static final ExceptionMessage CODE_EMPTY = new ExceptionMessage(1161, 400, "Code is empty");
    public static final ExceptionMessage TYPE_EMPTY = new ExceptionMessage(1162, 400, "Type is empty");
    public static final ExceptionMessage USER_TYPE_EMPTY = new ExceptionMessage(1163, 400, "User type is empty");
    public static final ExceptionMessage CODE_WORSE = new ExceptionMessage(1164, 400, "Verification code not correct try again");

}
