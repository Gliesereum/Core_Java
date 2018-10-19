package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 * @since 12/10/2018
 */
public class EmailExceptionMessage {

    public static final ExceptionMessage EMAIL_EMPTY = new ExceptionMessage(1240, 400, "Value email is empty");
    public static final ExceptionMessage EMAIL_CODE_EMPTY = new ExceptionMessage(1241, 400, "Value code by email is empty");
    public static final ExceptionMessage EMAIL_NOT_FOUND = new ExceptionMessage(1242, 400, "Email not found ");
    public static final ExceptionMessage EMAIL_EXIST = new ExceptionMessage(1243, 400, "Email already exist");
    public static final ExceptionMessage NOT_EMAIL_BY_REGEX = new ExceptionMessage(1244, 400, "Not valid email");
    public static final ExceptionMessage USER_ALREADY_HAS_EMAIL = new ExceptionMessage(1245, 400, "User already has some email");
    public static final ExceptionMessage USER_DOES_NOT_EMAIL = new ExceptionMessage(1246, 400, "User doesn't any email");
    public static final ExceptionMessage CAN_NOT_DELETE_EMAIL = new ExceptionMessage(1247, 400, "You can't delete email. You don't have phone. Email is only way to verify your account");


}
