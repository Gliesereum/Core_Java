package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 * @since 12/10/2018
 */
public class PhoneExceptionMessage {

    public static final ExceptionMessage PHONE_EMPTY = new ExceptionMessage(1220, 400, "Value phone number is empty");
    public static final ExceptionMessage PHONE_CODE_EMPTY = new ExceptionMessage(1221, 400, "Value code by phone is empty");
    public static final ExceptionMessage PHONE_NOT_FOUND = new ExceptionMessage(1222, 400, "Phone number not found ");
    public static final ExceptionMessage PHONE_EXIST = new ExceptionMessage(1223, 400, "Phone number already exist");
    public static final ExceptionMessage NOT_PHONE_BY_REGEX = new ExceptionMessage(1224, 400, "Not valid phone number");

}
