package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 * @since 12/10/2018
 */
public class PhoneExceptionMessage {

    public static final ExceptionMessage PHONE_EMPTY = new ExceptionMessage(1120, 400, "Value phone number is empty");
    public static final ExceptionMessage PHONE_CODE_EMPTY = new ExceptionMessage(1121, 400, "Value code by phone is empty");
    public static final ExceptionMessage PHONE_NOT_FOUND = new ExceptionMessage(1122, 400, "Phone number not found ");
    public static final ExceptionMessage PHONE_EXIST = new ExceptionMessage(1123, 400, "Phone number already exist");
    public static final ExceptionMessage NOT_PHONE_BY_REGEX = new ExceptionMessage(1124, 400, "Not valid phone number");
    public static final ExceptionMessage USER_ALREADY_HAS_PHONE = new ExceptionMessage(1125, 400, "User already has some phone");
    public static final ExceptionMessage USER_DOES_NOT_PHONE = new ExceptionMessage(1126, 400, "User doesn't any phone");
    public static final ExceptionMessage CAN_NOT_DELETE_PHONE = new ExceptionMessage(1127, 400, "You can't delete phone. You don't have email. Phone is only way to verify your account");

}
