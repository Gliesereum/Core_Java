package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 * @since 12/10/2018
 */
public class UserExceptionMessage {

    public static final ExceptionMessage USER_NOT_FOUND = new ExceptionMessage(1010, 400, "User not found ");
    public static final ExceptionMessage USER_NOT_AUTHENTICATION = new ExceptionMessage(1011, 400, "User not authentication ");
    public static final ExceptionMessage USER_ERROR_CHANGE_FIRST_NAME = new ExceptionMessage(1012, 400, "You can't change first name. Contact administrator");
    public static final ExceptionMessage USER_ERROR_CHANGE_LAST_NAME = new ExceptionMessage(1013, 400, "You can't change last name. Contact administrator");
    public static final ExceptionMessage UPL_AVATAR_IS_NOT_VALID = new ExceptionMessage(1014, 500, "Url avatar is not valid");
    public static final ExceptionMessage UPL_COVER_IS_NOT_VALID = new ExceptionMessage(1015, 500, "Url cover is not valid");
    public static final ExceptionMessage USER_NOT_BUSINESS = new ExceptionMessage(1016, 400, "User not business ");


}
